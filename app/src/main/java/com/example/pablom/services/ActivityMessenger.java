package com.example.pablom.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityMessenger extends AppCompatActivity {
    static final int MSG_SAY_HELLO_BACK = 2;
    final String CHANNEL_ID = "1";
    Messenger mService = null;

    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_service);

        // Se crea el canal necesario para mostrar la notificación
        createNotificationChannel();

        // Se enlaza la actividad al servicio al que se va a mandar el mensaje
        Intent i = new Intent(this, MessengerService.class);
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
        final Button sayHello = (Button)findViewById(R.id.sayHello);
        sayHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sayHello();
            }
        });
    }

    /**
     * Clase para interactuar con el servicio
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Se usa la interfaz recibida al conectarse al servicio para crear el Messenger al que se le van a mandar los mensajes
            mService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    /**
     * Clase para recibir la respuesta del servicio
     */
    class ResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO_BACK:
                    Bundle data = msg.getData();
                    NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("Hello")
                            .setContentText(data.getString("response"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                    // Se muestra la notificación creada con un identificador único
                    notificationManager.notify(NotificationID.getID(), nBuilder.build());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // Messenger que va a recibir la respuesta del servicio
    final Messenger mMessenger = new Messenger(new ResponseHandler());

    public void sayHello() {
        if (!mBound) return;
        Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO, 0,0);
        Bundle bundle = new Bundle();
        bundle.putString("saludo", "Hello world");
        msg.setData(bundle);
        msg.replyTo = mMessenger;

        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Al iniciar la actividad se enlaza la actividad con el servicio
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, MessengerService.class);
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "canalIPC";
            String description = "IPCBidireccional";
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
