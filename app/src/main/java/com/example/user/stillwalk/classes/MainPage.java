package com.example.user.stillwalk.classes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.stillwalk.R;

import java.util.Arrays;

public class MainPage extends AppCompatActivity {


    public static final String MyPREFERENCES = "LoginInfo";
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button personalInfo = findViewById(R.id.personal_info);
        Button contacts = findViewById(R.id.contacts);
        Button sos = findViewById(R.id.sos);
        Button signOut = findViewById(R.id.sign_out);
        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, PersonalDataPage.class);
            startActivity(intent);
        });

        sos.setOnClickListener(v -> {

                Intent intent = new Intent(MainPage.this, SmsPage.class);
                startActivity(intent);
        });

        contacts.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, ContactsPage.class);
            startActivity(intent);
        });

        signOut.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usernameKey","");

            editor.apply();
            startActivity(new Intent(MainPage.this,LoginPage.class));
        });

    }

    @Override
    public void onBackPressed() {
    }
}
