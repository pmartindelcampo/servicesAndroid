package com.example.pablom.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityDownload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Button download = (Button)findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se crea el Intent al servicio pasándole la URL del fichero y el tipo
                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                intent.setDataAndType(Uri.parse("https://sample-videos.com/pdf/Sample-pdf-5mb.pdf"), "application/pdf");

                startService(intent);
            }
        });
    }
}
