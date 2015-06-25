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
    private Point point;
    private Context context;

    public BusLineAdapter(Point point, Context context) {
        this.point = point;
        this.context = context;

        if(point == null) {
            this.busLines = new ArrayList<BusLine>();
        } else {
            this.busLines = point.getAvailableLines();
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

        BusTime busTime = point.getBusTime(ci.getNumber());
        String destination = ci.getName();
        Integer nextTime = null;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        String displayNextTime = context.getString(R.string.no_forecast);
        if( nextTime != null && nextTime > 0 ) {
            displayNextTime = nextTime.toString() + " min";
        }

        busLineViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busLineViewHolder.vDestination.setText(destination);
        busLineViewHolder.vNextTime.setText(displayNextTime);
        busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);

        busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(context, BusStopLineActivity.class);
            intent.putExtra("busLine", ci);
            intent.putExtra("point", point);
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
