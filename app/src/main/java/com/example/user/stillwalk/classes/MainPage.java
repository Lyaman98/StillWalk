package com.example.user.stillwalk.classes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.GetLocationService;
import com.example.user.stillwalk.helperclasses.User;

import java.util.Arrays;

public class MainPage extends AppCompatActivity {


    public static final String MyPREFERENCES = "LoginInfo";
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        CardView personalInfo = findViewById(R.id.personal_info);
        CardView contacts = findViewById(R.id.contacts);
        CardView sos = findViewById(R.id.sos);
        CardView signOut = findViewById(R.id.sign_out);
        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, PersonalDataPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        sos.setOnClickListener(v -> {

                Intent intent = new Intent(MainPage.this, SmsPage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        });

        contacts.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, ContactsPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        signOut.setOnClickListener(v -> {

            stopService(new Intent(this,GetLocationService.class));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usernameKey","");

            editor.apply();
            startActivity(new Intent(MainPage.this,LoginPage.class));
        });


        startService(new Intent(this, GetLocationService.class));


    }

    public void appInfo(View view){
        Intent intent = new Intent(this,StillWalkInfo.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}
