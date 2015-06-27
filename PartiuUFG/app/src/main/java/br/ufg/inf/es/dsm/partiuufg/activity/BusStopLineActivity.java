package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
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

public class BusStopLineActivity extends AbstractActivity {
    private BusLine busLine;
    private CompleteBusStop completeBusStop;

    private TextView tvShowTime;
    private TextView lineNumber ;
    private TextView lineName;
    private CheckedTextView checkGCMFav;

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

    public void loadView() {
        setTitle("Linha " + busLine.getNumber() + " do ponto " + completeBusStop.getNumber());
        lineNumber.setText(busLine.getNumber().toString());
        lineName.setText(busLine.getName());

        List<GCMBusPointTime> favorites = getLineFavorite();
        if (!checkPlayServices(false)) {
            if(favorites.size() > 0) {
                GCMBusPointTime.deleteAll(GCMBusPointTime.class);
            }
        } else {
            if (favorites.size() > 0) {
                checkGCMFav.setChecked(true);
            }
        }
        setTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvShowTime = (TextView) findViewById(R.id.tvTimeCount);
        checkGCMFav = (CheckedTextView) findViewById(R.id.gcmFavorite);
        lineNumber = (TextView) findViewById(R.id.lineNumber);
        lineName = (TextView) findViewById(R.id.lineName);

        busLine = (BusLine) getIntent().getSerializableExtra("busLine");
        completeBusStop = (CompleteBusStop) getIntent().getSerializableExtra("completeBusStop");

        if(completeBusStop == null || busLine == null) {
            final Integer busLineNumber = getIntent().getIntExtra("busLineNumber", 0);
            Integer busStopNumber = getIntent().getIntExtra("busStopNumber", 0);

            EasyBusService service = RestBusServiceFactory.getAdapter();
            service.getPoint(busStopNumber.toString(), new Callback<CompleteBusStop>() {
                @Override
                public void success(CompleteBusStop dCompleteBusStop, Response response) {
                    completeBusStop = dCompleteBusStop;
                    busLine = completeBusStop.getBusLine(busLineNumber);
                    loadView();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast toast = Toast.makeText(getBaseContext(),
                            getString(R.string.bus_stop_not_found),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } else {
            loadView();
        }
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
            }

            @Override
            public void onFinish() {
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
