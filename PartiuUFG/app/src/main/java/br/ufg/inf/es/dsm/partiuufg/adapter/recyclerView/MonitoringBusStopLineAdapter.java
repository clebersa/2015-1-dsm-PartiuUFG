package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.timer.ListAdapterRefreshTimer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 02/07/2015.
 */
public class MonitoringBusStopLineAdapter extends RecyclerView.Adapter<MonitoringBusStopLineAdapter.MonBusStopLineViewHolder> {
    private List<CompleteGCMBusStopLine> monBusStopLines;
    private Context context;
    private Timer timer;

    public MonitoringBusStopLineAdapter(List<CompleteGCMBusStopLine> monBusStopLines, Context context) {
        this.context = context;

        if(monBusStopLines == null) {
            this.monBusStopLines = new ArrayList<>();
        } else {
            this.monBusStopLines = monBusStopLines;
        }

        timer = new Timer();
        TimerTask updateData = new ListAdapterRefreshTimer(this);
        timer.scheduleAtFixedRate(updateData, 60000, 60000);
    }

    public CompleteGCMBusStopLine itemExists(SingleGCMBusStopLine item) {
        for( CompleteGCMBusStopLine monBusStopLine : monBusStopLines ) {
            if(monBusStopLine.compareSingle(item)) {
                return monBusStopLine;
            }
        }
        return null;
    }

    public void setMonBusStopLines(List<CompleteGCMBusStopLine> monBusStopLines) {
        this.monBusStopLines = monBusStopLines;
    }

    public void cancelRefreshTimer() {
        timer.cancel();
    }

    @Override
    public int getItemCount() {
        return monBusStopLines.size();
    }

    @Override
    public void onBindViewHolder(MonBusStopLineViewHolder monBusStopLineViewHolder, int i) {
        final CompleteGCMBusStopLine ci = monBusStopLines.get(i);

        monBusStopLineViewHolder.vBusLineNumber.setText(ci.getBusLineNumber().toString());
        monBusStopLineViewHolder.vBusStopNumber.setText(ci.getPointNumber().toString());

        if(!ci.isLoaded()){
            loadCompleteGCMBusStopLine(ci, i);
            monBusStopLineViewHolder.vDataContent.setVisibility(View.INVISIBLE);
            monBusStopLineViewHolder.vLoadingContent.setVisibility(View.VISIBLE);
        } else {
            final CompleteBusStop busStop = ci.getBusStop();
            final BusLine busLine = busStop.getBusLine(ci.getBusLineNumber());
            BusTime busTime = busStop.getBusTime(ci.getBusLineNumber());

            Integer nextTime = 0;
            if (busTime != null) {
                nextTime = busTime.getNextTime();
            }

            long currentTime = System.currentTimeMillis();
            long searchTime = busStop.getSearchDateTimestamp();
            long elapsedTime = currentTime - searchTime;
            long remainingTime = (60 * nextTime * 1000) - (elapsedTime + 1000);
            Double dRemainingMinutes = Math.ceil((Double.valueOf(remainingTime) / 60000));
            Integer remainingMinutes = dRemainingMinutes.intValue();
            if (remainingMinutes > nextTime) {
                remainingMinutes = nextTime;
            }

            if (remainingMinutes > 0) {
                monBusStopLineViewHolder.vNoPrevision.setVisibility(View.GONE);
                monBusStopLineViewHolder.vNextTime.setVisibility(View.VISIBLE);
                if (!remainingMinutes.equals(nextTime)) {
                    monBusStopLineViewHolder.vNextTimeAproxLabel.setVisibility(View.VISIBLE);
                }
            } else {
                monBusStopLineViewHolder.vNoPrevision.setVisibility(View.VISIBLE);
                monBusStopLineViewHolder.vNextTime.setVisibility(View.GONE);
                monBusStopLineViewHolder.vNextTimeAproxLabel.setVisibility(View.GONE);
            }

            String displayNextTime = context.getString(R.string.item_display_next_time,
                    remainingMinutes);
            String beforeMinutesToAlert = context.getString(R.string.item_before_time_to_alert,
                    ci.getBeforeMinutesToAlert());

            monBusStopLineViewHolder.vName.setText(busLine.getName().trim());
            monBusStopLineViewHolder.vAddress.setText(busStop.getAddress().trim());
            monBusStopLineViewHolder.vBeforeMinutesToAlert.setText(beforeMinutesToAlert);
            monBusStopLineViewHolder.vNextTime.setText(displayNextTime);

            monBusStopLineViewHolder.vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusStopLineActivity.class);
                    intent.putExtra("busLine", (Serializable) busLine);
                    intent.putExtra("completeBusStop", (Serializable) busStop);
                    context.startActivity(intent);
                }
            });

            monBusStopLineViewHolder.vDataContent.setVisibility(View.VISIBLE);
            monBusStopLineViewHolder.vLoadingContent.setVisibility(View.GONE);
        }
    }

    @Override
    public MonBusStopLineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.item_monitoring_bus_stop_line, viewGroup, false);
        return new MonBusStopLineViewHolder(itemView);
    }

    public void loadCompleteGCMBusStopLine(final CompleteGCMBusStopLine ci, final int position) {
        final MonitoringBusStopLineAdapter fAdapter = this;
        EasyBusService service = RestBusServiceFactory.getAdapter();
        service.getPoint(ci.getPointNumber().toString(), new Callback<CompleteBusStop>() {
            @Override
            public void success(CompleteBusStop completeBusStop, Response response) {
                ci.setBusStop(completeBusStop);
                ci.setLoaded(true);
                fAdapter.notifyItemChanged(position);
                fAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static class MonBusStopLineViewHolder extends RecyclerView.ViewHolder {
        public View vItem;
        public View vDataContent;
        public View vLoadingContent;
        public TextView vBusLineNumber;
        public TextView vBusStopNumber;
        public TextView vName;
        public TextView vAddress;
        public TextView vBeforeMinutesToAlert;
        public ImageView vNoPrevision;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;

        public MonBusStopLineViewHolder(View v) {
            super(v);

            vItem = v;
            vDataContent = v.findViewById(R.id.data_content);
            vLoadingContent = v.findViewById(R.id.loading_content);
            vBusLineNumber = (TextView) v.findViewById(R.id.bus_line_number);
            vBusStopNumber = (TextView) v.findViewById(R.id.bus_stop_number);
            vName = (TextView) v.findViewById(R.id.name);
            vAddress = (TextView) v.findViewById(R.id.address);
            vBeforeMinutesToAlert = (TextView) v.findViewById(R.id.before_minutes_to_alert);
            vNoPrevision = (ImageView) v.findViewById(R.id.no_prevision);
            vNextTime = (TextView) v.findViewById(R.id.next_time);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.next_time_aprox_label);
        }
    }
}
