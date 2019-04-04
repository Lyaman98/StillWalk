package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.user.stillwalk.R;

public class IntroPage extends AppCompatActivity {


    static boolean isShowed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        Handler handler = new Handler();

        handler.postDelayed(() -> {

            isShowed = true;
            Intent loginIntent = new Intent(this, LoginPage.class);
            startActivity(loginIntent);

        }, 3000);
    }
}
