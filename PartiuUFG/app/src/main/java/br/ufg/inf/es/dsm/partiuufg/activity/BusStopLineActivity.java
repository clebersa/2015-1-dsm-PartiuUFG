package br.ufg.inf.es.dsm.partiuufg.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
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
    private Boolean isFavorite = false;
    private Boolean isLoadedFromWeb;

    private List<SingleGCMBusStopLine> getLineFavorite() {
        List<SingleGCMBusStopLine> singleGcmBusStopLines = SingleGCMBusStopLine.find(SingleGCMBusStopLine.class,
                "point_number = ? and bus_line_number = ?",
                busStopNumber.toString(),
                busLineNumber.toString());

        return singleGcmBusStopLines;
    }

    private void deleteStopLineFavorite() {
        for( SingleGCMBusStopLine singleGcmBusStopLine : getLineFavorite() ) {
            singleGcmBusStopLine.delete();
        }
    }

    private void addStopLineFavorite() {
        SingleGCMBusStopLine singleGcmBusStopLine = new SingleGCMBusStopLine(busStopNumber,
                busLineNumber);
        singleGcmBusStopLine.save();
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

    private void getIntentObjects() {
        isLoadedFromWeb = false;
        busLine = (BusLine) getIntent().getSerializableExtra("busLine");
        completeBusStop = (CompleteBusStop) getIntent().getSerializableExtra("completeBusStop");

        if(completeBusStop == null || busLine == null) {
            busLineNumber = getIntent().getIntExtra("busLineNumber", 0);
            busStopNumber = getIntent().getIntExtra("busStopNumber", 0);
            isLoadedFromWeb = true;
        } else {
            busLineNumber = busLine.getNumber();
            busStopNumber = completeBusStop.getNumber();
        }
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

        getIntentObjects();
        beforeViewCreated();

        if(isLoadedFromWeb) {
            getBusStopLineFromWeb();
        } else {
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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bus_stop_line, menu);

        List<SingleGCMBusStopLine> favorites = getLineFavorite();
        if (favorites.size() > 0) {
            if (!checkPlayServices(false)) {
                SingleGCMBusStopLine.deleteAll(SingleGCMBusStopLine.class);
            } else {
                MenuItem favItem = menu.findItem(R.id.action_favorite);
                favItem.setIcon(R.drawable.ic_action_action_alarm_on);
                isFavorite = true;
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.action_favorite:
                if (checkPlayServices(true)) {
                    if (isFavorite) {
                        deleteStopLineFavorite();
                        item.setIcon(R.drawable.ic_action_action_alarm_off);
                        isFavorite = false;
                    } else {
                        addStopLineFavorite();
                        item.setIcon(R.drawable.ic_action_action_alarm_on);
                        isFavorite = true;
                    }
                } else {
                    Toast toast = Toast.makeText(getBaseContext(),
                            getString(R.string.no_play_service_installed),
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        super.onQueryTextSubmit(query);
        finish();
        return false;
    }
}
