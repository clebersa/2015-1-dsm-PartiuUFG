package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
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
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteGCMBusStopLine;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 02/07/2015.
 */
public class MonitoringBusLineStopAdapter extends RecyclerView.Adapter<MonitoringBusLineStopAdapter.MonBusStopLineViewHolder> {
    private static final String TAG = MonitoringBusLineStopAdapter.class.getSimpleName();
    private List<CompleteGCMBusStopLine> monBusStopLines;
    private Context context;

    public MonitoringBusLineStopAdapter(List<CompleteGCMBusStopLine> monBusStopLines, Context context) {
        this.context = context;

        if(monBusStopLines == null) {
            this.monBusStopLines = new ArrayList<>();
        } else {
            this.monBusStopLines = monBusStopLines;
        }
    }

    public List<CompleteGCMBusStopLine> getMonBusStopLines() {
        return monBusStopLines;
    }

    public Boolean itemExists(SingleGCMBusStopLine item) {
        for( CompleteGCMBusStopLine monBusStopLine : monBusStopLines ) {
            if(monBusStopLine.compareSingle(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return monBusStopLines.size();
    }

    @Override
    public void onBindViewHolder(MonBusStopLineViewHolder monBusStopLineViewHolder, int i) {
        final CompleteGCMBusStopLine ci = monBusStopLines.get(i);
        final MonitoringBusLineStopAdapter fAdapter = this;
        final int position = i;

        if(!ci.isLoaded()) {
            EasyBusService service = RestBusServiceFactory.getAdapter();
            service.getPoint(ci.getPointNumber().toString(), new Callback<CompleteBusStop>() {
                @Override
                public void success(CompleteBusStop completeBusStop, Response response) {
                    ci.setAddress(completeBusStop.getAddress());
                    ci.setSearchDate(completeBusStop.getSearchDate());
                    BusLine busLine = completeBusStop.getBusLine(ci.getBusLineNumber());
                    if (busLine != null) {
                        ci.setName(busLine.getName());
                    }
                    BusTime busTime = completeBusStop.getBusTime(ci.getBusLineNumber());
                    if (busTime != null) {
                        ci.setNextTime(busTime.getNextTime());
                    }
                    ci.setLoaded(true);
                    fAdapter.notifyItemChanged(position);
                    fAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

        Integer nextTime = 0;
        if (ci.getNextTime() != null) {
            nextTime = ci.getNextTime();
        }

        String displayNextTime = context.getString(R.string.no_forecast);
        if (nextTime > 0) {
            displayNextTime = context.getString(R.string.item_display_next_time, nextTime);
        }

        String beforeMinutesToAlert = context.getString( R.string.item_before_time_to_alert,
                ci.getBeforeMinutesToAlert());

        monBusStopLineViewHolder.vLineNumber.setText(ci.getBusLineNumber().toString());
        monBusStopLineViewHolder.vBusStopNumber.setText(ci.getPointNumber().toString());
        monBusStopLineViewHolder.vName.setText(ci.getName());
        monBusStopLineViewHolder.vAddress.setText(ci.getAddress());
        monBusStopLineViewHolder.vNextTime.setText(displayNextTime);
        monBusStopLineViewHolder.vBeforeMinutesToAlert.setText(beforeMinutesToAlert);

        monBusStopLineViewHolder.vItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusStopLineActivity.class);
                intent.putExtra("busLineNumber", ci.getBusLineNumber());
                intent.putExtra("busStopNumber", ci.getPointNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public MonBusStopLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.monitoring_bus_stop_line_item, viewGroup, false);
        return new MonBusStopLineViewHolder(itemView);
    }

    public static class MonBusStopLineViewHolder extends RecyclerView.ViewHolder {
        public View vItem;
        public TextView vLineNumber;
        public TextView vBusStopNumber;
        public TextView vName;
        public TextView vAddress;
        public TextView vNextTime;
        public TextView vBeforeMinutesToAlert;

        public MonBusStopLineViewHolder(View v) {
            super(v);

            vItem = v;
            vLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vBusStopNumber = (TextView) v.findViewById(R.id.bus_stop_number);
            vName = (TextView) v.findViewById(R.id.name);
            vAddress = (TextView) v.findViewById(R.id.address);
            vNextTime = (TextView) v.findViewById(R.id.next_time);
            vBeforeMinutesToAlert = (TextView) v.findViewById(R.id.before_minutes_to_alert);
        }
    }
}
