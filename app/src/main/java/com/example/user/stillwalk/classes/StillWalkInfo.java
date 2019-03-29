package com.example.user.stillwalk.classes;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.user.stillwalk.R;

public class StillWalkInfo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appinfo);

        TextView instructions = findViewById(R.id.instructions);

        instructions.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}
