package br.ufg.inf.es.dsm.partiuufg.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import br.ufg.inf.es.dsm.partiuufg.http.GCMHttpService;
import br.ufg.inf.es.dsm.partiuufg.http.RestGCMServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.GCMMessage;
import br.ufg.inf.es.dsm.partiuufg.model.GCMResult;
import br.ufg.inf.es.dsm.partiuufg.model.GCMMessageData;

public class GCMServer extends Service {
    private static final String TAG = GCMServer.class.getSimpleName();
    private boolean isRunning  = false;
    private String deviceToken;

    public GCMServer() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Serviço criado");
        isRunning = true;
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getBaseContext());
        deviceToken= sharedPreferences.getString("gcmToken", null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isRunning){
                    GCMMessageData data = new GCMMessageData("ônibus 105 no ponto 6363 passa em 5 minutos", 6363, 105);
                    GCMMessage GCMMessage = new GCMMessage(deviceToken, data);

                    GCMHttpService service = RestGCMServiceFactory.getAdapter();
                    GCMResult resposta = service.send(GCMMessage);
                    Log.i(TAG, resposta.toString());
                }

                stopSelf();
            }
        }).start();

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;

        Log.i(TAG, "onDestroy");
    }
}
