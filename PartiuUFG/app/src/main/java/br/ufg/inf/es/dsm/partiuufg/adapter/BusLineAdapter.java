package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import br.ufg.inf.es.dsm.partiuufg.timer.ListAdapterRefreshTimer;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusLineAdapter extends RecyclerView.Adapter<BusLineAdapter.BusLineViewHolder> implements Serializable {
    private List<BusLine> busLines;
    private CompleteBusStop completeBusStop;
    private Context context;
    private Timer timer;

    public BusLineAdapter(CompleteBusStop completeBusStop, Context context) {
        this.completeBusStop = completeBusStop;
        this.context = context;

        this.busLines = new ArrayList<>();
        if(completeBusStop != null) {
            for( BusLine busLine : completeBusStop.getAvailableLines()) {
                this.busLines.add(busLine);
            }
        }

        timer = new Timer();
        TimerTask updateData = new ListAdapterRefreshTimer(this);
        timer.scheduleAtFixedRate(updateData, 60000, 60000);
    }

    public void cancelRefreshTimer() {
        timer.cancel();
    }

    public List<BusLine> getBusLines() {
        return busLines;
    }

    @Override
    public int getItemCount() {
        return busLines.size();
    }

    @Override
    public void onBindViewHolder(BusLineViewHolder busLineViewHolder, int i) {
        final BusLine ci = busLines.get(i);

        BusTime busTime = completeBusStop.getBusTime(ci.getNumber());
        String destination = ci.getName();
        Integer nextTime = 0;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        long currentTime = System.currentTimeMillis();
        long searchTime = completeBusStop.getSearchDateTimestamp();
        long elapsedTime = currentTime - searchTime;
        long remainingTime = (60 * nextTime * 1000) - elapsedTime;
        Integer remainingMinutes = (int) (remainingTime / 60000);

        busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
        String displayNextTime = context.getString(R.string.no_forecast);
        if( remainingMinutes > 0 ) {
            displayNextTime = remainingMinutes.toString() + " min";
            if(!remainingMinutes.equals(nextTime)) {
                busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.VISIBLE);
            }
        }

        busLineViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busLineViewHolder.vDestination.setText(destination);
        busLineViewHolder.vNextTime.setText(displayNextTime);

        busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, BusStopLineActivity.class);
            intent.putExtra("busLine", ci);
            intent.putExtra("completeBusStop", completeBusStop);
            context.startActivity(intent);
            }
        });
    }

    @Override
    public BusLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.bus_time_card, viewGroup, false);
        return new BusLineViewHolder(itemView);
    }

    public static class BusLineViewHolder extends RecyclerView.ViewHolder {
        public View vCard;
        public TextView vLineNumber;
        public TextView vDestination;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;

        public BusLineViewHolder(View v) {
            super(v);

            vCard = v;
            vLineNumber = (TextView) v.findViewById(R.id.lineNumber);
            vDestination = (TextView) v.findViewById(R.id.destination);
            vNextTime = (TextView) v.findViewById(R.id.nextTime);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.nextTimeAproxLabel);
        }
    }
}
