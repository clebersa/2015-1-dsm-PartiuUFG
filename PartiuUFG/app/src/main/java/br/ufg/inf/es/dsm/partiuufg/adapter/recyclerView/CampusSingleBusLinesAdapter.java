package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusLineActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;

/**
 * Created by Cleber on 21/06/2015.
 */
public class CampusSingleBusLinesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = CampusSingleBusLinesAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Object> singleBusLines;
    private Context context;

    public CampusSingleBusLinesAdapter(List<Object> singleBusLines, Context context) {
        this.context = context;

        if(singleBusLines == null) {
            this.singleBusLines = new ArrayList<>();
        } else {
            this.singleBusLines = singleBusLines;
        }
    }

    public List<Object> getSingleBusLines() {
        return singleBusLines;
    }

    @Override
    public int getItemCount() {
        return singleBusLines.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CampusBusLineItemViewHolder) {
            final SingleBusLine busLine = (SingleBusLine) singleBusLines.get(i);
            CampusBusLineItemViewHolder busLineViewHolder = (CampusBusLineItemViewHolder) viewHolder;

            busLineViewHolder.vLineNumber.setText(busLine.getNumber().toString());
            if(busLine.getName() != null) {
                busLineViewHolder.vDestination.setText(busLine.getName());
            }
            busLineViewHolder.vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusLineActivity.class);
                    intent.putExtra("lineNumber", busLine.getNumber());
                    context.startActivity(intent);
                }
            });
        } else if (viewHolder instanceof CampusBusLineHeaderViewHolder) {
            Campus campus = (Campus) singleBusLines.get(i);
            CampusBusLineHeaderViewHolder campusVH = (CampusBusLineHeaderViewHolder) viewHolder;
            campusVH.vName.setText(campus.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        if(singleBusLines.get(position) instanceof Campus) {
            return true;
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        if(viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_bus_line_without_bus_stop,
                    viewGroup, false);
            return new CampusBusLineItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.campus_bus_line_header,
                    viewGroup, false);
            return new CampusBusLineHeaderViewHolder(itemView);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType +
                " make sure your using types correctly");
    }

    public static class CampusBusLineItemViewHolder extends RecyclerView.ViewHolder {
        public View vItem;
        public TextView vLineNumber;
        public TextView vDestination;

        public CampusBusLineItemViewHolder(View v) {
            super(v);
            vItem = v;
            vLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vDestination = (TextView) v.findViewById(R.id.name);
        }
    }

    public static class CampusBusLineHeaderViewHolder extends RecyclerView.ViewHolder {
        public View vItem;
        public TextView vName;

        public CampusBusLineHeaderViewHolder(View v) {
            super(v);
            vItem = v;
            vName = (TextView) v.findViewById(R.id.name);
        }
    }
}
