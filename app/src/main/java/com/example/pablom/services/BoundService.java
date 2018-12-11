package com.example.pablom.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Random;

public class BoundService extends Service {

    private final IBinder mBinder = new ServiceBinder();

    private final Random mGenerator = new Random();

    /**
     * Clase usada para enlazar la actividad
     */
    public class ServiceBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service iniciado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service finalizado", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Método que usará la actividad enlazada
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}
