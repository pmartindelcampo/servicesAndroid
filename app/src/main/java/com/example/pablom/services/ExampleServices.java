package com.example.pablom.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ExampleServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_services);

        Button exampleIntentService = (Button)findViewById(R.id.intentService);
        Button exampleService = (Button)findViewById(R.id.service);
        exampleIntentService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ExampleIntentService.class);
                startService(i);
            }
        });
        exampleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ExampleService.class);
                startService(i);
            }
        });
    }
}
