package com.example.pablom.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class ForegroundService extends Service {

    final String CHANNEL_ID = "2";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            if (intent.getExtras().getInt("stop") == 2) {
                stopSelf();
            }
        } else {
            Toast.makeText(this, "Servicio iniciado", Toast.LENGTH_SHORT).show();
            // Se crea el canal necesario para mostrar la notificación
            createNotificationChannel();
            startForeground(1, buildNotification());
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio finalizado", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification() {
        Intent nIntent = new Intent(this, ForegroundActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0,nIntent, 0);
        Intent intent = new Intent(this, ForegroundService.class);
        intent.putExtra("stop", 2);
        PendingIntent pauseIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Reproduciendo...")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_stop, "Parar", pauseIntent);
        return builder.build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "canalMusic";
            String description = "Music";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
