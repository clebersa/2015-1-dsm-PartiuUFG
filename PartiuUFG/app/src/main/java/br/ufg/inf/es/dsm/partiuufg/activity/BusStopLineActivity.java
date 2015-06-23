package br.ufg.inf.es.dsm.partiuufg.activity;

import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;

public class BusStopLineActivity extends AbstractActivity {
    private BusLine busLine;
    private Integer pointNumber;
    private TextView tvShowTime;
    private ProgressBar pbNextBusTime;

    private long totalTimeCountInMilliseconds; // total count down time in miliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        busLine = (BusLine) getIntent().getSerializableExtra("buLine");
        pointNumber = getIntent().getIntExtra( "pointNumber", -1 );

        tvShowTime = (TextView) findViewById(R.id.tvTimeCount);
        pbNextBusTime = (ProgressBar) findViewById(R.id.progressbar);

        setTimer();
    }

    private void setTimer() {
        BusTime busTime = busLine.getPoint(pointNumber).getBusTime(busLine.getNumber());
        Integer nextTime = 0;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        totalTimeCountInMilliseconds = 60 * nextTime * 1000;
        pbNextBusTime.setMax(60 * nextTime);

        startTimer();
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                //Setting the Progress Bar to decrease wih the timer
                pbNextBusTime.setProgress((int) (leftTimeInMilliseconds / 1000));

                tvShowTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {
                tvShowTime.setText("Passando agora...");
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
