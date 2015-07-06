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

/**
 * Created by Bruno on 06/07/2015.
 */
public class BusTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> busTimeTable;

    public BusTimeAdapter(List<String> busTimeTable) {
        if(busTimeTable == null) {
            this.busTimeTable = new ArrayList<>();
        } else {
            this.busTimeTable = busTimeTable;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_bus_time,
                parent, false);
        return new BusTimeVH(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String time = busTimeTable.get(position);
        BusTimeVH VH = (BusTimeVH) holder;
        VH.vTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return busTimeTable.size();
    }

    public static class BusTimeVH extends RecyclerView.ViewHolder {
        public View vItem;
        public TextView vTime;

        public BusTimeVH(View v) {
            super(v);
            vItem = v;
            vTime = (TextView) v.findViewById(R.id.time);
        }
    }
}
