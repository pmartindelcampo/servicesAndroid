package com.example.pablom.services;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exampleServices = (Button)findViewById(R.id.exampleService);
        exampleServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ExampleServices.class);
                startActivity(i);
            }
        });

        Button boundServices = (Button)findViewById(R.id.boundService);
        boundServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BindingActivity.class);
                startActivity(i);
            }
        });

        Button interconnexion = (Button)findViewById(R.id.ipc);
        interconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityMessenger.class);
                startActivity(i);
            }
        });

        Button foreground = (Button)findViewById(R.id.foreground);
        foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForegroundActivity.class);
                startActivity(i);
            }
        });

        Button download = (Button)findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityDownload.class);
                startActivity(i);
            }
        });
    }
}
