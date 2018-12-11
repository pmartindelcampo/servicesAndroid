package com.example.pablom.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MessengerService extends Service {

    static final int MSG_SAY_HELLO = 1;

    // Messenger de la actividad a la que se va a contestar
    Messenger mActivity;

    /**
     *  Manejador de los mensajes que se van a recibir
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Bundle data = msg.getData();
                    String hello = data.getString("saludo");
                    Toast.makeText(getApplicationContext(), hello, Toast.LENGTH_SHORT).show();
                    // Con el atributo "replyTo" se obtiene el Messenger al que hay que responder
                    mActivity = msg.replyTo;
                    sendResponse();
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // Messenger publicado para que otros procesos manden mensajes
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Cuando se enlaza el servicio con una actividad, se devuelve una interfaz del Messenger para poder mandar mensajes
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private void sendResponse() {
        Message msg = Message.obtain(null, ActivityMessenger.MSG_SAY_HELLO_BACK, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putString("response", "Hello back");
        msg.setData(bundle);

        try {
            mActivity.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
