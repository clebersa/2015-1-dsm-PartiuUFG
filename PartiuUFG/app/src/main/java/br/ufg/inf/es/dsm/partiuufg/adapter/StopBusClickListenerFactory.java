package br.ufg.inf.es.dsm.partiuufg.adapter;

import android.content.Context;
import android.view.View;

import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusStop;

/**
 * Created by Bruno on 29/06/2015.
 */
public interface StopBusClickListenerFactory {
    View.OnClickListener createClickListener(Context context, SingleBusStop busStop);
}
