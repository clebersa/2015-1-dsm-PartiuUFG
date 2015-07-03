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
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;

/**
 * Created by Cleber on 21/06/2015.
 */
public class CampusSingleBusLinesAdapter extends RecyclerView.Adapter<CampusSingleBusLinesAdapter.CampusBusLineViewHolder> {
    private final String TAG = CampusSingleBusLinesAdapter.class.getSimpleName();

    private List<SingleBusLine> singleBusLines;
    private Context context;

    public CampusSingleBusLinesAdapter(List<SingleBusLine> singleBusLines, Context context) {
        this.context = context;

        if(singleBusLines == null) {
            this.singleBusLines = new ArrayList<>();
        } else {
            this.singleBusLines = singleBusLines;
        }
    }

    public List<SingleBusLine> getSingleBusLines() {
        return singleBusLines;
    }

    @Override
    public int getItemCount() {
        return singleBusLines.size();
    }

    @Override
    public void onBindViewHolder(CampusBusLineViewHolder busLineViewHolder, int i) {
        final SingleBusLine busLine = singleBusLines.get(i);

        busLineViewHolder.vLineNumber.setText(busLine.getNumber().toString());
        if(busLine.getName() != null) {
            busLineViewHolder.vDestination.setText(busLine.getName());
        }

        busLineViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusLineActivity.class);
                intent.putExtra("lineNumber", busLine.getNumber());
                context.startActivity(intent);
            }
        });
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
