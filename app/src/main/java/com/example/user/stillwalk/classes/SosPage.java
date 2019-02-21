package com.example.user.stillwalk.classes;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.stillwalk.R;

public class SosPage extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_page);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        mp.start();

    }
}
