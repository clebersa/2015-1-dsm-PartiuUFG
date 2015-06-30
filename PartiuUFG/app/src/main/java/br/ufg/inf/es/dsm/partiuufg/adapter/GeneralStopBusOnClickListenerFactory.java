package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.activity.BusStopActivity;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by cleber on 27/06/15.
 */
public class GeneralStopBusOnClickListenerFactory implements StopBusClickListenerFactory {
    @Override
    public View.OnClickListener createClickListener(Context context, SingleBusStop busStop) {
        final Context fContext = context;
        final SingleBusStop fSingleBusStop = busStop;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fContext, BusStopActivity.class);
                intent.putExtra("pointId", fSingleBusStop.getNumber());
                fContext.startActivity(intent);
            }
        };

        return onClickListener;
    }
}
