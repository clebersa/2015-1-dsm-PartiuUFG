package br.ufg.inf.es.dsm.partiuufg.timer;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.TimerTask;

import br.ufg.inf.es.dsm.partiuufg.adapter.BusLineAdapter;

/**
 * Created by Bruno on 27/06/2015.
 */
public class ListAdapterRefreshTimer extends TimerTask {
    private RecyclerView.Adapter adapter;
    private Handler mHandler = new Handler();

    public ListAdapterRefreshTimer(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
