package com.example.pablom.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ExampleService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler que recibe los mensajes desde el hilo principal
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Se para el servicio con el startId del hilo, para no parar el servicio si otro hilo lo está usando
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Se inicia el hilo donde se ejecutará el servicio, ya que por defecto el servicio se ejecutaría en el hilo principal
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service iniciado", Toast.LENGTH_SHORT).show();
        // Para cada petición, se manda un mensaje para iniciar el proceso del servicio
        // y se manda el startId para saber que petición se está deteniendo
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // Si el proceso se detiene mientras se está iniciando, se reinicia
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service finalizado", Toast.LENGTH_SHORT).show();
    }
}
