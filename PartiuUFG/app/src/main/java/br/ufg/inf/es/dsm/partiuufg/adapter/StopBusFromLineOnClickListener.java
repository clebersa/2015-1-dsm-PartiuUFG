package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Intent;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;

/**
 * Created by cleber on 27/06/15.
 */
public class StopBusFromLineOnClickListener extends StopBusOnClickListener{

    private Integer busLineNumber;

    public StopBusFromLineOnClickListener(BusStopAdapter busStopAdapter, Integer busLineNumber) {
        super(busStopAdapter);
        this.busLineNumber = busLineNumber;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(busStopAdapter.getContext(), BusStopLineActivity.class);
        intent.putExtra("busLineNumber", busLineNumber);
        intent.putExtra("busStopNumber", busStopAdapter.getBusStops().get(index).getNumber());
        busStopAdapter.getContext().startActivity(intent);
    }
}
