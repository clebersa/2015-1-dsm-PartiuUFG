package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusLineAdapter extends RecyclerView.Adapter<BusLineAdapter.BusLineViewHolder> {
    private List<BusLine> busLines;
    private Integer pointNumber;
    private Context context;

    public BusLineAdapter(List<BusLine> busLines, Integer pointNumber, Context context) {
        this.pointNumber = pointNumber;
        this.context = context;

        if(busLines == null) {
            this.busLines = new ArrayList<>();
        } else {
            this.busLines = busLines;
        }
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
        final TextView vNextTime = busLineViewHolder.vNextTime;
        final TextView vNextTimeAproxLabel = busLineViewHolder.vNextTimeAproxLabel;

        BusTime busTime = ci.getPoint(pointNumber).getBusTime(ci.getNumber());
        String destination = ci.getName();
        Integer nextTime = null;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        String displayNextTime = context.getString(R.string.no_forecast);
        if( nextTime != null && nextTime > 0 ) {
            displayNextTime = nextTime.toString() + " min";

            long totalTime = nextTime * 60000;
            busLineViewHolder.countDownTimer = new CountDownTimer(totalTime, 60000) {
                @Override
                public void onTick(long leftTimeInMilliseconds) {
                    int minutes = Math.round(leftTimeInMilliseconds / 60000);
                    String displayNextTime = minutes + " min";
                    vNextTime.setText(displayNextTime);
                    vNextTimeAproxLabel.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    vNextTime.setText(context.getString(R.string.no_forecast));
                }
            };
            busLineViewHolder.countDownTimer.start();
        }

        busLineViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busLineViewHolder.vDestination.setText(destination);
        busLineViewHolder.vNextTime.setText(displayNextTime);
        busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);

        busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopLineActivity.class);
                intent.putExtra("buLine", ci);
                intent.putExtra("pointNumber", pointNumber);
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
        public CountDownTimer countDownTimer;

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
