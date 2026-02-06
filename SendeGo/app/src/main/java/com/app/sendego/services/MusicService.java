package com.app.sendego.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.app.sendego.R;

import java.util.Random;

public class MusicService extends Service {

    private MediaPlayer myPlayer;

    private final int[] songs = {
            R.raw.spongebob_music_chase,
            R.raw.queens_garden_hk,
            R.raw.moog_city_mc
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Random random = new Random();
        int indice = random.nextInt(songs.length);

        myPlayer = MediaPlayer.create(this, songs[indice]);
        myPlayer.setLooping(true);
        myPlayer.setVolume(100, 100);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myPlayer != null && !myPlayer.isPlaying()) {
            myPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myPlayer != null) {
            if (myPlayer.isPlaying()) {
                myPlayer.stop();
            }
            myPlayer.release();
            myPlayer = null;
        }
    }
}
