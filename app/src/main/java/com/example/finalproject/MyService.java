package com.example.finalproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MyService extends Service {

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate () {
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
        mediaPlayer.setLooping(true);
    }

    public void onStart (Intent intent , int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        mediaPlayer.start();
    }

    public void onDestroy () {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        mediaPlayer.stop();
    }
}
