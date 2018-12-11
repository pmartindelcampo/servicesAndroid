package com.example.pablom.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ExampleIntentService extends IntentService {

    /**
     * Es necesario un constructor que llame al constructor padre con el nombre del hilo
     */
    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    /**
     *  Se llama a este método desde el hilo que lanzó el intent para iniciar el servicio.
     *  Cuando esté método termina, se detiene el servicio
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //
       /* Handler mHandler = new Handler(getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "IntentService iniciado", Toast.LENGTH_SHORT).show();
            }
        });*/
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        /*mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "IntentService finalizado", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "IntentService iniciado", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "IntentService finalizado", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
