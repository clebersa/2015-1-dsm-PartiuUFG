package br.ufg.inf.es.dsm.partiuufg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GCMServer extends Service {
    private static final String GOOGLE_API_KEY =  "AIzaSyCYKPh_MMBMaF1EFgQVkmu6sgLYuFhMUoE";
    private static final String TAG = "GCMService";
    private boolean isRunning  = false;

    public GCMServer() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Serviço criado");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 50; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    if(isRunning){
                        Log.i(TAG, "Serviço rodando");
                    }
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
