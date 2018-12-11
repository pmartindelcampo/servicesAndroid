package com.example.pablom.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pablom.services.BoundService.ServiceBinder;

public class BindingActivity extends AppCompatActivity {
    BoundService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound_service);
        Button buttonGenerate = (Button)findViewById(R.id.generatenumber);
        final TextView tvNumber = (TextView)findViewById(R.id.number);

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    // Se llama a un método del servicio
                    // Si el método fuera largo sería necesario ejecutar el método en otro hilo
                    int num = mService.getRandomNumber();
                    tvNumber.setText(String.valueOf(num));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, BoundService.class);
        // Se enlaza el servicio con la actividad
        bindService(i, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            // Se desenlaza el servicio
            unbindService(mConnection);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Después de hacer el cast del IBinder a ServiceBinder podemos obtener el servicio
            ServiceBinder binder = (ServiceBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}
