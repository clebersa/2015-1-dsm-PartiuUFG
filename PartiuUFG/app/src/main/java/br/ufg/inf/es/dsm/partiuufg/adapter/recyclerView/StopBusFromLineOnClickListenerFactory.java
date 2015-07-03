package br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by cleber on 27/06/15.
 */
public class StopBusFromLineOnClickListenerFactory implements StopBusClickListenerFactory {
    private Integer busLineNumber;

    public StopBusFromLineOnClickListenerFactory(Integer busLineNumber) {
        this.busLineNumber = busLineNumber;
    }

    @Override
    public View.OnClickListener createClickListener(Context context, SingleBusStop busStop) {
        final Context fContext = context;
        final SingleBusStop fSingleBusStop = busStop;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fContext, BusStopLineActivity.class);
                intent.putExtra("busLineNumber", busLineNumber);
                intent.putExtra("busStopNumber", fSingleBusStop.getNumber());
                fContext.startActivity(intent);
            }
        };

        return onClickListener;
    }
}
