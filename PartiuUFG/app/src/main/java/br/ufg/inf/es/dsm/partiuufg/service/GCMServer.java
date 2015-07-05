package br.ufg.inf.es.dsm.partiuufg.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleGCMBusStopLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.GCMHttpService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.http.RestGCMServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusTime;
import br.ufg.inf.es.dsm.partiuufg.model.GCMMessage;
import br.ufg.inf.es.dsm.partiuufg.model.GCMMessageData;

public class GCMServer extends Service {
    private static final String TAG = GCMServer.class.getSimpleName();
    private static final Long SLEEP_TIME = (long) 55000;
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
                while(true) {
                    if (isRunning) {
                        List<SingleGCMBusStopLine> alertBusStopLines = null;
                        try {
                            alertBusStopLines = SingleGCMBusStopLine.listAll(SingleGCMBusStopLine.class);
                        } catch(Exception e) {
                            Log.e(TAG, "Can't read alert bus stop lines...");
                            try {
                                Thread.sleep(SLEEP_TIME);
                            } catch (InterruptedException e1) {}
                            continue;
                        }
                        EasyBusService easyBusService = RestBusServiceFactory.getAdapter();
                        for (final SingleGCMBusStopLine alertBusStopLine : alertBusStopLines) {
                            try {
                                BusTime busTime = easyBusService.getBusNextTimeInPoint(
                                        alertBusStopLine.getPointNumber().toString(),
                                        alertBusStopLine.getBusLineNumber().toString());

                                if (busTime.getNextTime() <= alertBusStopLine.getBeforeMinutesToAlert()) {
                                    String message = getString(R.string.GCMNotificationMessage,
                                            alertBusStopLine.getBusLineNumber(),
                                            busTime.getNextTime(),
                                            alertBusStopLine.getPointNumber());
                                    GCMMessageData data = new GCMMessageData(message,
                                            alertBusStopLine.getPointNumber(),
                                            alertBusStopLine.getBusLineNumber());
                                    GCMMessage GCMMessage = new GCMMessage(deviceToken, data);

                                    Log.d(TAG, "Enviando mensagem...");
                                    GCMHttpService service = RestGCMServiceFactory.getAdapter();
                                    service.send(GCMMessage);
                                    Log.d(TAG, "mensagem enviada...");
                                }
                            } catch(Exception e) {
                                Log.e(TAG, "erro: " + e.getMessage());
                            }
                        }
                    }

                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {}
                }
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
