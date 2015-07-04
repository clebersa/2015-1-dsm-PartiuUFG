package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

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
public class BusLineAdapter implements ParallaxRecyclerAdapter.RecyclerAdapterMethods {
    private CompleteBusStop completeBusStop;
    private ParallaxRecyclerAdapter adapter;
    private Context context;
    private Timer timer;

    public BusLineAdapter(ParallaxRecyclerAdapter<BusLine> adapter, CompleteBusStop completeBusStop,
                          Context context) {
        this.adapter = adapter;
        this.completeBusStop = completeBusStop;
        this.context = context;

        timer = new Timer();
        TimerTask updateData = new ListAdapterRefreshTimer(adapter);
        timer.scheduleAtFixedRate(updateData, 60000, 60000);
    }

    public static List<BusLine> getCreatedData(CompleteBusStop completeBusStop) {
        List<BusLine> busLines = new ArrayList<>();
        if(completeBusStop != null) {
            for( BusLine busLine : completeBusStop.getAvailableLines()) {
                busLines.add(busLine);
            }
        }
        return busLines;
    }

    public void cancelRefreshTimer() {
        timer.cancel();
    }

    @Override
    public int getItemCount() {
        return adapter.getData().size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final BusLine ci = (BusLine) adapter.getData().get(i);
        BusLineViewHolder busLineViewHolder = (BusLineViewHolder) viewHolder;

        BusTime busTime = completeBusStop.getBusTime(ci.getNumber());
        Integer nextTime = 0;
        if( busTime != null ) {
            nextTime = busTime.getNextTime();
        }

        long currentTime = System.currentTimeMillis();
        long searchTime = completeBusStop.getSearchDateTimestamp();
        long elapsedTime = currentTime - searchTime;
        long remainingTime = (60 * nextTime * 1000) - (elapsedTime + 2000);
        Double dRemainingMinutes = Math.ceil((Double.valueOf(remainingTime) / 60000));
        Integer remainingMinutes = dRemainingMinutes.intValue();
        if(remainingMinutes > nextTime) {
            remainingMinutes = nextTime;
        }

        busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
        String displayNextTime = context.getString(R.string.item_display_next_time, remainingMinutes);
        if( remainingMinutes > 0 ) {
            if(!remainingMinutes.equals(nextTime)) {
                busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.VISIBLE);
            }
        }

        busLineViewHolder.vLineNumber.setText(ci.getNumber().toString());
        busLineViewHolder.vName.setText(ci.getName());
        busLineViewHolder.vNextTime.setText(displayNextTime);

        busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopLineActivity.class);
                intent.putExtra("busLine", (Serializable) ci);
                intent.putExtra("completeBusStop", (Serializable) completeBusStop);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.item_bus_line_with_bus_stop, viewGroup, false);
        return new BusLineViewHolder(itemView);
    }

    public static class BusLineViewHolder extends RecyclerView.ViewHolder {
        public View vCard;
        public TextView vLineNumber;
        public TextView vName;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;

        public BusLineViewHolder(View v) {
            super(v);

            vCard = v;
            vLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vName = (TextView) v.findViewById(R.id.name);
            vNextTime = (TextView) v.findViewById(R.id.next_time);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.next_time_aprox_label);
        }
    }
}
