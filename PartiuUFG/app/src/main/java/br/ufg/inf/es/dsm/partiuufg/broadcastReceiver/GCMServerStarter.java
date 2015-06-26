package br.ufg.inf.es.dsm.partiuufg.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.ufg.inf.es.dsm.partiuufg.service.GCMServer;

/**
 * Created by bruno on 6/26/15.
 */
public class GCMServerStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("teste", "=========== STARTANDO SERVIDOR GCM ============");
        Intent gcmServerIntent = new Intent(context, GCMServer.class);
        context.startService(gcmServerIntent);
    }
}
