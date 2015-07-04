package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusStopViewHolder> {
    private List<SingleBusStop> busStops;
    StopBusClickListenerFactory clickListenerFactory;
    private Context context;

    public BusStopAdapter(List<SingleBusStop> busStops, Context context) {
        this.context = context;

        if(busStops == null) {
            this.busStops = new ArrayList<>();
        } else {
            this.busStops = busStops;
        }
    }

    public List<SingleBusStop> getBusStops() {
        return busStops;
    }

    public Context getContext() {
        return context;
    }

    public void setOnClickListenerFactory(StopBusClickListenerFactory factory) {
        this.clickListenerFactory = factory;
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    @Override
    public void onBindViewHolder(BusStopViewHolder busStopViewHolder, int i) {
        final SingleBusStop ci = busStops.get(i);

        busStopViewHolder.vBusStopNumber.setText(ci.getNumber().toString());
        busStopViewHolder.vAddress.setText(ci.getAddress());
        busStopViewHolder.vReference.setText(ci.getReference());

        if ("".equals(ci.getReference())) {
            busStopViewHolder.vReference.setVisibility(View.GONE);
        }

        busStopViewHolder.vCard.setOnClickListener(clickListenerFactory.createClickListener(
                getContext(), ci));
    }

    @Override
    public BusStopViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.item_bus_stop_without_bus_line, viewGroup, false);
        return new BusStopViewHolder(itemView);
    }

    public static class BusStopViewHolder extends RecyclerView.ViewHolder {
        public View vCard;
        public TextView vBusStopNumber;
        public TextView vAddress;
        public TextView vReference;

        public BusStopViewHolder(View v) {
            super(v);

            vCard = v;
            vBusStopNumber = (TextView) v.findViewById(R.id.bus_stop_number);
            vAddress = (TextView) v.findViewById(R.id.address);
            vReference = (TextView) v.findViewById(R.id.reference);
        }
    }
}
