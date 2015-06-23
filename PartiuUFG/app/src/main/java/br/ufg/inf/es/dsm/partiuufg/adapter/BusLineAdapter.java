package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.Point;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusLineAdapter extends RecyclerView.Adapter<BusLineAdapter.BusLineViewHolder> {
    private List<BusLine> busLines;
    private Integer pointNumber;

    public BusLineAdapter(List<BusLine> busLines, Integer pointNumber) {
        this.pointNumber = pointNumber;

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
        BusLine ci = busLines.get(i);

        BusTime busTime = ci.getPoint(pointNumber).getBusTime(ci.getNumber());
        String destination = ci.getName();
        Integer nextTime = null;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        String displayNextTime = busLineViewHolder.context.getString(R.string.no_forecast);
        if( nextTime != null && nextTime > 0 ) {
            displayNextTime = nextTime.toString() + " min";
        }

        busLineViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busLineViewHolder.vDestination.setText(destination);
        busLineViewHolder.vNextTime.setText(displayNextTime);
        busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
    }

    @Override
    public BusLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.bus_time_card, viewGroup, false);

        return new BusLineViewHolder(itemView, context);
    }

    public static class BusLineViewHolder extends RecyclerView.ViewHolder {
        public TextView vLineNumber;
        public TextView vDestination;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;
        public final Context context;

        public BusLineViewHolder(View v, final Context context) {
            super(v);
            vLineNumber = (TextView) v.findViewById(R.id.lineNumber);
            vDestination = (TextView) v.findViewById(R.id.destination);
            vNextTime = (TextView) v.findViewById(R.id.nextTime);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.nextTimeAproxLabel);
            this.context = context;
        }
    }
}
