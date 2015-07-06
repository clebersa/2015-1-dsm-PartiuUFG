package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
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
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopActivity;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusStopWithLine;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.CompleteBusStop;
import br.ufg.inf.es.dsm.partiuufg.timer.ListAdapterRefreshTimer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 21/06/2015.
 */
public class BusStopAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_BUS_STOP_WITH_LINE = 0;
    private static final int ITEM_BUS_STOP_WITHOUT_LINE = 1;

    private List<T> busStops;
    private Integer busLineNumber;
    private Timer timer;
    private int position;

    public BusStopAdapter(List<T> busStops, Integer busLineNumber) {
        this.busLineNumber = busLineNumber;

        if(busStops == null) {
            this.busStops = new ArrayList<>();
        } else {
            this.busStops = busStops;
        }

        timer = new Timer();
        if(busLineNumber != null && busLineNumber >= 0) {
            TimerTask updateData = new ListAdapterRefreshTimer(this);
            timer.scheduleAtFixedRate(updateData, 60000, 60000);
        }
    }

    public void setBusStops(List<T> busStops) {
        this.busStops = busStops;
    }

    public List<T> getBusStops() {
        return busStops;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void cancelRefreshTimer() {
        timer.cancel();
    }

    @Override
    public int getItemCount() {
        return busStops.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof VHBusStopWithBusLine) {
            VHBusStopWithBusLine vhBusStopWithBusLine = (VHBusStopWithBusLine) viewHolder;
            BusStopWithLine busStop = (BusStopWithLine)busStops.get(i);
            if(!busStop.isLoaded()) {
                vhBusStopWithBusLine.loadBusStopLine(busStop, i, this);
            }
            vhBusStopWithBusLine.createView(busStop, busLineNumber);
        } else if(viewHolder instanceof VHBusStopWithoutBusLine) {
            final VHBusStopWithoutBusLine vhBusStopWithoutBusLine = (VHBusStopWithoutBusLine) viewHolder;
            SingleBusStop busStop = (SingleBusStop) busStops.get(i);
            vhBusStopWithoutBusLine.createView(busStop);
            vhBusStopWithoutBusLine.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosition(vhBusStopWithoutBusLine.getAdapterPosition());
                    return false;
                }
            });
        } else {
            throw new RuntimeException("there is no support to the requested view holder");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View itemView;
        switch(viewType) {
            case ITEM_BUS_STOP_WITH_LINE:
                itemView = LayoutInflater.from(context)
                        .inflate(R.layout.item_bus_stop_with_bus_line, viewGroup, false);
                return new VHBusStopWithBusLine(itemView, context);
            case ITEM_BUS_STOP_WITHOUT_LINE:
                itemView = LayoutInflater.from(context)
                        .inflate(R.layout.item_bus_stop_without_bus_line, viewGroup, false);
                return new VHBusStopWithoutBusLine(itemView, context);
            default:
                throw new RuntimeException("there is no type that matches the type " + viewType +
                        " make sure your using types correctly");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(busLineNumber == null && (busStops.get(position) instanceof SingleBusStop)) {
            return ITEM_BUS_STOP_WITHOUT_LINE;
        } else if(busLineNumber > 0 && (busStops.get(position) instanceof BusStopWithLine)) {
            return ITEM_BUS_STOP_WITH_LINE;
        }

        throw new RuntimeException("there is no type that matches the item: " + position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class VHBusStopWithoutBusLine extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final Context context;
        public View vItem;
        public TextView vBusStopNumber;
        public TextView vAddress;
        public TextView vReference;

        public VHBusStopWithoutBusLine(View v, Context context) {
            super(v);
            this.context = context;

            vItem = v;
            vBusStopNumber = (TextView) v.findViewById(R.id.bus_stop_number);
            vAddress = (TextView) v.findViewById(R.id.address);
            vReference = (TextView) v.findViewById(R.id.reference);
            vItem.setOnCreateContextMenuListener(this);
        }

        public void createView(final SingleBusStop busStop) {
            vBusStopNumber.setText(busStop.getNumber().toString());
            vAddress.setText(busStop.getAddress().trim());
            vReference.setText(busStop.getReference().trim());

            if (busStop.getReference().equals("")) {
                vReference.setVisibility(View.GONE);
            }

            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusStopActivity.class);
                    intent.putExtra("pointId", busStop.getNumber());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(context.getString(R.string.context_menu_favorite_bus_stop));
            menu.add(Menu.NONE, 1, Menu.NONE, "Remover");
        }
    }

    public static class VHBusStopWithBusLine extends RecyclerView.ViewHolder {
        public final Context context;
        public View vItem;
        public View vDataContent;
        public View vLoadingContent;
        public TextView vBusStopNumber;
        public TextView vAddress;
        public TextView vReference;
        public ImageView vNoPrevision;
        public TextView vNextTime;
        public TextView vNextTimeAproxLabel;

        public VHBusStopWithBusLine(View v, Context context) {
            super(v);
            this.context = context;

            vItem = v;
            vDataContent = v.findViewById(R.id.data_content);
            vLoadingContent = v.findViewById(R.id.loading_content);
            vBusStopNumber = (TextView) v.findViewById(R.id.bus_stop_number);
            vAddress = (TextView) v.findViewById(R.id.address);
            vReference = (TextView) v.findViewById(R.id.reference);
            vNoPrevision = (ImageView) v.findViewById(R.id.no_prevision);
            vNextTime = (TextView) v.findViewById(R.id.next_time);
            vNextTimeAproxLabel = (TextView) v.findViewById(R.id.next_time_aprox_label);
        }

        public void createView(final BusStopWithLine busStop, final Integer busLineNumber) {
            vBusStopNumber.setText(busStop.getNumber().toString());
            vAddress.setText(busStop.getAddress().trim());
            vReference.setText(busStop.getReference().trim());

            if (busStop.getReference().equals("")) {
                vReference.setVisibility(View.GONE);
            } else {
                vReference.setVisibility(View.VISIBLE);
            }

            if (!busStop.isLoaded()) {
                vItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, BusStopLineActivity.class);
                        intent.putExtra("busLineNumber", busLineNumber);
                        intent.putExtra("busStopNumber", busStop.getNumber());
                        context.startActivity(intent);
                    }
                });
                vDataContent.setVisibility(View.INVISIBLE);
                vLoadingContent.setVisibility(View.VISIBLE);
            } else {
                final CompleteBusStop completeBusStop = busStop.getCompleteBusStop();
                final BusLine busLine = completeBusStop.getBusLine(busLineNumber);
                BusTime busTime = completeBusStop.getBusTime(busLineNumber);

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
                    vNoPrevision.setVisibility(View.GONE);
                    vNextTime.setVisibility(View.VISIBLE);
                    if (!remainingMinutes.equals(nextTime)) {
                        vNextTimeAproxLabel.setVisibility(View.VISIBLE);
                    } else {
                        vNextTimeAproxLabel.setVisibility(View.GONE);
                    }
                } else {
                    vNoPrevision.setVisibility(View.VISIBLE);
                    vNextTime.setVisibility(View.GONE);
                    vNextTimeAproxLabel.setVisibility(View.GONE);
                }

                String displayNextTime = context.getString(R.string.item_display_next_time,
                        remainingMinutes);
                vNextTime.setText(displayNextTime);

                vItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, BusStopLineActivity.class);
                        intent.putExtra("busLine", (Serializable) busLine);
                        intent.putExtra("completeBusStop", (Serializable) completeBusStop);
                        context.startActivity(intent);
                    }
                });
                vDataContent.setVisibility(View.VISIBLE);
                vLoadingContent.setVisibility(View.GONE);
            }
        }

        public void loadBusStopLine(final BusStopWithLine busStop, final int position,
                                    final BusStopAdapter fAdapter) {
            final View loadErrorMessage = vLoadingContent.findViewById(R.id.load_error_message);
            final View loadProgressBar = vLoadingContent.findViewById(R.id.loading_progress_bar);
            loadErrorMessage.setVisibility(View.GONE);
            loadProgressBar.setVisibility(View.VISIBLE);
            EasyBusService service = RestBusServiceFactory.getAdapter();
            service.getPoint(busStop.getNumber().toString(), new Callback<CompleteBusStop>() {
                @Override
                public void success(CompleteBusStop completeBusStop, Response response) {
                    busStop.setCompleteBusStop(completeBusStop);
                    busStop.setLoaded(true);
                    fAdapter.notifyItemChanged(position);
                    fAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    loadErrorMessage.setVisibility(View.VISIBLE);
                    loadProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
