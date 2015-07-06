package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        adapter.implementRecyclerAdapterMethods(this);
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

    public void setCompleteBusStop(CompleteBusStop completeBusStop) {
        this.completeBusStop = completeBusStop;
    }

    public void setAdapter(ParallaxRecyclerAdapter adapter) {
        this.adapter = adapter;
        this.adapter.implementRecyclerAdapterMethods(this);
    }

    @Override
    public int getItemCount() {
        return adapter.getData().size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(completeBusStop == null) {
            return;
        }

        BusLineViewHolder busLineViewHolder = (BusLineViewHolder) viewHolder;
        final BusLine busLine = (BusLine) adapter.getData().get(i);
        BusTime busTime = completeBusStop.getBusTime(busLine.getNumber());

        Integer nextTime = 0;
        if (busTime != null) {
            nextTime = busTime.getNextTime();
        }

        long currentTime = System.currentTimeMillis();
        long searchTime = completeBusStop.getSearchDateTimestamp();
        long elapsedTime = currentTime - searchTime;
        long remainingTime = (60 * nextTime * 1000) - (elapsedTime + 1000);
        Double dRemainingMinutes = Math.ceil((Double.valueOf(remainingTime) / 60000));
        Integer remainingMinutes = dRemainingMinutes.intValue();
        if (remainingMinutes > nextTime) {
            remainingMinutes = nextTime;
        }

        if (remainingMinutes > 0) {
            busLineViewHolder.vNoPrevision.setVisibility(View.GONE);
            busLineViewHolder.vNextTime.setVisibility(View.VISIBLE);
            if (!remainingMinutes.equals(nextTime)) {
                busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.VISIBLE);
            } else {
                busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
            }
        } else {
            busLineViewHolder.vNoPrevision.setVisibility(View.VISIBLE);
            busLineViewHolder.vNextTime.setVisibility(View.GONE);
            busLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
        }

        String displayNextTime = context.getString(R.string.item_display_next_time,
                remainingMinutes);

        busLineViewHolder.vBusLineNumber.setText(busLine.getNumber().toString());
        busLineViewHolder.vName.setText(busLine.getName().trim());
        busLineViewHolder.vNextTime.setText(displayNextTime);

        busLineViewHolder.vItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopLineActivity.class);
                intent.putExtra("busLine", (Serializable) busLine);
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
        public View vItem;
        public TextView vBusLineNumber;
        public TextView vName;
        public ImageView vNoPrevision;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;

        public BusLineViewHolder(View v) {
            super(v);

            vItem = v;
            vBusLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vName = (TextView) v.findViewById(R.id.name);
            vNoPrevision = (ImageView) v.findViewById(R.id.no_prevision);
            vNextTime = (TextView) v.findViewById(R.id.next_time);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.next_time_aprox_label);
        }
    }
}
