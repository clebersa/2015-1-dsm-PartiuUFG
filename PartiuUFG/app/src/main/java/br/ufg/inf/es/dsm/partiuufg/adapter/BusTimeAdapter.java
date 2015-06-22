package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusTimeAdapter extends RecyclerView.Adapter<BusTimeAdapter.BusTimeViewHolder> {
    private List<BusTime> timeList;

    public BusTimeAdapter(List<BusTime> timeList) {
        if(timeList == null) {
            this.timeList = new ArrayList<BusTime>();
        } else {
            this.timeList = timeList;
        }

        this.timeList.add(new BusTime(302, "PC Campus", 30, -1));
        this.timeList.add(new BusTime(174, "PC Campus", 30, -1));
        this.timeList.add(new BusTime(270, "PC Campus", 30, -1));
        this.timeList.add(new BusTime(238, "PC Campus", 30, -1));
        this.timeList.add(new BusTime(263, "PC Campus", 30, -1));
    }

    public List<BusTime> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<BusTime> timeList) {
        this.timeList = timeList;
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    @Override
    public void onBindViewHolder(BusTimeViewHolder busTimeViewHolder, int i) {
        BusTime ci = timeList.get(i);
        busTimeViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busTimeViewHolder.vDestination.setText(ci.getDestination());
    }

    @Override
    public BusTimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.bus_time_card, viewGroup, false);

        return new BusTimeViewHolder(itemView, context);
    }

    public static class BusTimeViewHolder extends RecyclerView.ViewHolder {
        public TextView vLineNumber;
        public TextView vDestination;
        public final Context context;

        public BusTimeViewHolder(View v, final Context context) {
            super(v);
            vLineNumber = (TextView) v.findViewById(R.id.lineNumber);
            vDestination = (TextView) v.findViewById(R.id.destination);
            this.context = context;
        }
    }
}
