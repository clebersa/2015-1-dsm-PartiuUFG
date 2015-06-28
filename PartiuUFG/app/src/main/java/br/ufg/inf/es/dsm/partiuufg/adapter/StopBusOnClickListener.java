package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.view.View;

/**
 * Created by cleber on 27/06/15.
 */
public abstract class StopBusOnClickListener implements View.OnClickListener {
    protected Integer index;
    protected BusStopAdapter busStopAdapter;

    public StopBusOnClickListener(BusStopAdapter busStopAdapter) {
        this.busStopAdapter = busStopAdapter;
    }

    public final void setIndex(Integer index) {
        this.index = index;
    }
}
