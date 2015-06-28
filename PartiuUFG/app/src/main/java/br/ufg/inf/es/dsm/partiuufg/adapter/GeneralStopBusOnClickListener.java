package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Intent;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.activity.BusStopActivity;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;

/**
 * Created by cleber on 27/06/15.
 */
public class GeneralStopBusOnClickListener extends StopBusOnClickListener{

    public GeneralStopBusOnClickListener(BusStopAdapter busStopAdapter) {
        super(busStopAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(busStopAdapter.getContext(), BusStopActivity.class);
        intent.putExtra("pointId", busStopAdapter.getBusStops().get(index).getNumber());
        busStopAdapter.getContext().startActivity(intent);
    }
}
