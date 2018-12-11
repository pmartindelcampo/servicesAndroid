package com.example.pablom.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {

    final String CHANNEL_ID = "3";
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID+".provider";

    public DownloadService() {
        super("Download thread");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Iniciando descarga", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Se obtiene el nombre del fichero del último segmento de la URL
        String filename = intent.getData().getLastPathSegment();
        createNotificationChannel();
        // Se pone el servicio en primer plano creando la notificación
        startForeground(2, buildNotification());

        try {
            File f = new File(getFilesDir(), filename);

            if (f.exists()) {
                f.delete();
            }
                // Se obtiene la URL del intent y se conecta
                URL url = new URL(intent.getData().toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                FileOutputStream fos = new FileOutputStream(f.getPath());
                BufferedOutputStream out = new BufferedOutputStream(fos);

                try {
                    InputStream in = connection.getInputStream();
                    byte[] buffer = new byte[8192];
                    int length;

                    while ((length = in.read(buffer)) >= 0) {
                        out.write(buffer, 0, length);
                    }

                    out.flush();
                } finally {
                    // Una vez terminado la descarga, se crea una nueva notificación para indicar que se ha terminado
                    downloadComplete(intent, f, null);
                }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            downloadComplete(intent, null, e);
        } finally {
            // Una vez termina el proceso, se pasa el proceso a segundo palno
            stopForeground(false);
        }
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setOngoing(true)
                .setContentTitle("Descargando...")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(android.R.drawable.stat_sys_download);

        return builder.build();
    }

    private void downloadComplete(Intent i, File f, Exception e) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis());

        if (e == null) {
            builder.setContentTitle("Descarga finalizada")
                    .setSmallIcon(android.R.drawable.stat_sys_download_done);

            Intent outbound = new Intent(Intent.ACTION_VIEW);
            Uri outputUri = FileProvider.getUriForFile(this, AUTHORITY, f);

            outbound.setDataAndType(outputUri, i.getType());
            outbound.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Se crea un intent hacia el fichero descargado
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, outbound, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pIntent);
        } else {
            builder.setContentTitle("Error en la descarga")
                    .setContentText(e.getMessage())
                    .setSmallIcon(android.R.drawable.stat_notify_error);
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // Se muestra la nueva notificación
        notificationManager.notify(NotificationID.getID(), builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "canalDownload";
            String description = "Download";
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
