package com.example.pablom.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ForegroundActivity extends AppCompatActivity {

    private Menu menu;
    private MenuItem play, pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground);

        Toolbar toolbar = (Toolbar) findViewById(R.id.layout_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        this.menu = menu;
        play = menu.findItem(R.id.play);
        pause = menu.findItem(R.id.pause);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.play:
                reproducir();
                break;
            case R.id.pause:
                pararMusica();
                break;
        }
        return true;
    }

    private void reproducir() {
        Intent i = new Intent(this, ForegroundService.class);
        startService(i);
        play.setVisible(false);
        pause.setVisible(true);
    }

    public void pararMusica() {
        Intent i = new Intent(this, ForegroundService.class);
        stopService(i);
        play.setVisible(true);
        pause.setVisible(false);
    }
}
