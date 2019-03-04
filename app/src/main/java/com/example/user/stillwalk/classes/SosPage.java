package com.example.user.stillwalk.classes;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.stillwalk.R;

public class SosPage extends AppCompatActivity {

    private TextView firstName;
    private TextView lastName;
    private TextView personalInfo;
    private TextView age;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "PersonalInfo" ;
    public static final String USERNAME_PREFERENCES = "LoginInfo" ;
    String username;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_page);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        personalInfo = findViewById(R.id.personal_info);
        age = findViewById(R.id.age);


        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        mp.start();


        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCES, MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey", "");

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if (!TextUtils.isEmpty(sharedPreferences.getString("firstNameKey", ""))
                && sharedPreferences.getString("usernameKey", "").equals(username)) {

            firstName.setText(sharedPreferences.getString("firstNameKey", ""));

            lastName.setText(sharedPreferences.getString("lastNameKey", ""));
            age.setText(String.valueOf(sharedPreferences.getInt("ageKey", 0)));
            personalInfo.setText(sharedPreferences.getString("personalInfoKey", ""));
        }
    }
}
