package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

public class BusStopLineActivity extends AbstractActivity {
    private BusLine busLine;
    private Point point;

    private TextView tvShowTime;
    private CheckedTextView checkGCMFav;

    public void gcmFavorited(View view) {
        if( checkGCMFav.isChecked() ) {
            checkGCMFav.setChecked(false);
        } else {
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
