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
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;

/**
 * Created by Cleber on 21/06/2015.
 */
public class CampusSingleBusLinesAdapter extends RecyclerView.Adapter<CampusSingleBusLinesAdapter.CampusBusLineViewHolder> {
    private List<BusLine> busLines;
    private Context context;

    public CampusSingleBusLinesAdapter(List<BusLine> busLines, Context context) {
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
    public void onBindViewHolder(CampusBusLineViewHolder busLineViewHolder, int i) {
        final BusLine busLine = busLines.get(i);

        busLineViewHolder.vLineNumber.setText(busLine.getNumber().toString());
        busLineViewHolder.vDestination.setText(busLine.getName());

        /*busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusLineActivity.class);
                intent.putExtra("busLine", busLine.getNumber());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public CampusBusLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.bus_line_card, viewGroup, false);
        return new CampusBusLineViewHolder(itemView);
    }

    public static class CampusBusLineViewHolder extends RecyclerView.ViewHolder {
        public View vCard;
        public TextView vLineNumber;
        public TextView vDestination;

        public CampusBusLineViewHolder(View v) {
            super(v);
            vCard = v;
            vLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vDestination = (TextView) v.findViewById(R.id.bus_line_name);
        }
    }
}
