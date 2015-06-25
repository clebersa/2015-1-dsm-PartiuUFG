package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.dbModel.GCMBusPointTime;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

public class BusStopLineActivity extends AbstractActivity {
    private BusLine busLine;
    private Point point;

    private TextView tvShowTime;
    private CheckedTextView checkGCMFav;

    private List<GCMBusPointTime> getLineFavorite() {
        List<GCMBusPointTime> gcmBusPointTimes = GCMBusPointTime.find(GCMBusPointTime.class,
                "point_number = ? and bus_line_number = ?",
                point.getNumber().toString(),
                busLine.getNumber().toString());

        return gcmBusPointTimes;
    }

    private void deleteStopLineFavorite() {
        for( GCMBusPointTime gcmBusPointTime : getLineFavorite() ) {
            gcmBusPointTime.delete();
        }
    }

    private void addStopLineFavorite() {
        GCMBusPointTime gcmBusPointTime = new GCMBusPointTime(point.getNumber(),
                busLine.getNumber());
        gcmBusPointTime.save();
    }

    public void gcmFavorite(View view) {
        if( checkGCMFav.isChecked() ) {
            deleteStopLineFavorite();
            checkGCMFav.setChecked(false);
        } else {
            addStopLineFavorite();
            checkGCMFav.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        busLine = (BusLine) getIntent().getSerializableExtra("busLine");
        point = (Point) getIntent().getSerializableExtra("point");

        tvShowTime = (TextView) findViewById(R.id.tvTimeCount);
        checkGCMFav = (CheckedTextView) findViewById(R.id.gcmFavorite);

        TextView lineNumber = (TextView) findViewById(R.id.lineNumber);
        lineNumber.setText(busLine.getNumber().toString());
        TextView lineName = (TextView) findViewById(R.id.lineName);
        lineName.setText(busLine.getName());

        setTimer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if( getLineFavorite().size() > 0 ) {
            checkGCMFav.setChecked(true);
        }
    }

    private void setTimer() {
        BusTime busTime = point.getBusTime(busLine.getNumber());
        Integer nextTime = 0;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        long totalTime = 60 * nextTime * 1000;

        startTimer(totalTime);
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
