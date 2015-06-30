package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.dbModel.GCMBusPointTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tr.xip.errorview.ErrorView;

public class BusStopLineActivity extends AbstractActivity {
    private BusLine busLine;
    private Integer busLineNumber;
    private CompleteBusStop completeBusStop;
    private Integer busStopNumber;

    private TextView tvShowTime;
    private TextView lineNumber;
    private TextView lineName;
    private TextView aboutNextMinutes;
    private CheckedTextView checkGCMFav;

    private LinearLayout loadingContent;
    private ErrorView errorView;
    private LinearLayout content;

    private Integer showErrorStatus = 0;

    private List<GCMBusPointTime> getLineFavorite() {
        List<GCMBusPointTime> gcmBusPointTimes = GCMBusPointTime.find(GCMBusPointTime.class,
                "point_number = ? and bus_line_number = ?",
                completeBusStop.getNumber().toString(),
                busLine.getNumber().toString());

        return gcmBusPointTimes;
    }

    private void deleteStopLineFavorite() {
        for( GCMBusPointTime gcmBusPointTime : getLineFavorite() ) {
            gcmBusPointTime.delete();
        }
    }

    private void addStopLineFavorite() {
        GCMBusPointTime gcmBusPointTime = new GCMBusPointTime(completeBusStop.getNumber(),
                busLine.getNumber());
        gcmBusPointTime.save();
    }

    public void gcmFavorite(View view) {
        if (checkPlayServices(true)) {
            if (checkGCMFav.isChecked()) {
                deleteStopLineFavorite();
                checkGCMFav.setChecked(false);
            } else {
                addStopLineFavorite();
                checkGCMFav.setChecked(true);
            }
        } else {
            Toast toast = Toast.makeText(getBaseContext(),
                    getString(R.string.no_play_service_installed),
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void beforeViewCreated() {
        loadingContent.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
    }

    public void createView() {
        setTitle(getString(R.string.title_activity_bus_stop_line,busLine.getNumber(),
                completeBusStop.getNumber()));
        lineNumber.setText(busLine.getNumber().toString());
        lineName.setText(busLine.getName());

        List<GCMBusPointTime> favorites = getLineFavorite();
        if (favorites.size() > 0) {
            if (!checkPlayServices(false)) {
                GCMBusPointTime.deleteAll(GCMBusPointTime.class);
            } else {
                checkGCMFav.setChecked(true);
            }
        }
        setTimer();
        content.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        afterViewCreated();
    }

    public void showErrorView() {
        errorView.setError(showErrorStatus);
        content.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        afterViewCreated();
    }

    public void refreshView() {
        beforeViewCreated();
        getBusStopLineFromWeb();
    }

    public void afterViewCreated() {
        loadingContent.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingContent = (LinearLayout) findViewById(R.id.loading_content);
        content = (LinearLayout) findViewById(R.id.content);
        errorView = (ErrorView) findViewById(R.id.error_view);
        errorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                refreshView();
            }
        });

        tvShowTime = (TextView) findViewById(R.id.tvTimeCount);
        checkGCMFav = (CheckedTextView) findViewById(R.id.gcmFavorite);
        lineNumber = (TextView) findViewById(R.id.lineNumber);
        lineName = (TextView) findViewById(R.id.lineName);
        aboutNextMinutes = (TextView) findViewById(R.id.about_next_minutes);

        busLine = (BusLine) getIntent().getSerializableExtra("busLine");
        completeBusStop = (CompleteBusStop) getIntent().getSerializableExtra("completeBusStop");

        beforeViewCreated();

        if(completeBusStop == null || busLine == null) {
            busLineNumber = getIntent().getIntExtra("busLineNumber", 0);
            busStopNumber = getIntent().getIntExtra("busStopNumber", 0);
            getBusStopLineFromWeb();
        } else {
            busLineNumber = busLine.getNumber();
            busStopNumber = completeBusStop.getNumber();
            createView();
        }
    }

    public void getBusStopLineFromWeb() {
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getPoint(busStopNumber.toString(), new Callback<CompleteBusStop>() {
            @Override
            public void success(CompleteBusStop dCompleteBusStop, Response response) {
                completeBusStop = dCompleteBusStop;
                busLine = completeBusStop.getBusLine(busLineNumber);
                createView();
            }

            @Override
            public void failure(RetrofitError error) {
                if( error.getResponse() == null ) {
                    showErrorStatus = 408;
                } else {
                    showErrorStatus = error.getResponse().getStatus();
                }
                showErrorView();
            }
        });
    }

    private void setTimer() {
        BusTime busTime = completeBusStop.getBusTime(busLine.getNumber());
        Integer nextTime = 0;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        long currentTime = System.currentTimeMillis();
        long searchTime = completeBusStop.getSearchDateTimestamp();
        long elapsedTime = currentTime - searchTime;
        long remainingTime = (60 * nextTime * 1000) - elapsedTime;

        startTimer(remainingTime);
    }

    private void startTimer(long totalTime) {
        CountDownTimer countDownTimer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;

                tvShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));

                if(aboutNextMinutes.getVisibility() == View.GONE){
                    aboutNextMinutes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                tvShowTime.setText(getString(R.string.no_forecast));
                tvShowTime.setTextSize(getResources().getDimension(R.dimen.bus_timer_no_forecast));
                aboutNextMinutes.setVisibility(View.GONE);
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void setActivityContentView() {
        setContentView(R.layout.activity_bus_stop_line);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        finish();
        return false;
    }
}
