package br.ufg.inf.es.dsm.partiuufg.service;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.BusStopLineActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Bruno on 26/06/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static String TAG = MyGcmListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Integer busStopNumber = Integer.valueOf(data.getString("bus_stop"));
        Integer busLineNumber = Integer.valueOf(data.getString("bus_line"));

        sendNotification(message, busStopNumber, busLineNumber);
    }

    private void sendNotification(String message, Integer busStopNumber, Integer busLineNumber) {
        Intent targetIntent = new Intent(this, BusStopLineActivity.class);
        targetIntent.putExtra("busStopNumber", busStopNumber);
        targetIntent.putExtra("busLineNumber", busLineNumber);

        Integer notificationId = Integer.valueOf(busStopNumber.toString() + busLineNumber.toString());

        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId, targetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(android.R.color.transparent);
        }

        Notification notification = builder.build();

        NotificationManager nManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        nManager.notify(notificationId, notification);
    }
}
