package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;

import java.util.Timer;
import java.util.TimerTask;

public class SosPage extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    public static final String USERNAME_PREFERENCES = "LoginInfo" ;
    private MediaPlayer mp;
    String username;
    private CountDownTimer timer;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_page);
        TextView firstName = findViewById(R.id.firstName);
        TextView lastName = findViewById(R.id.lastName);
        TextView personalInfo = findViewById(R.id.personal_info);
        TextView age = findViewById(R.id.age);
        TextView contact1 = findViewById(R.id.contact1);
        TextView contact2 = findViewById(R.id.contact2);

         mp = MediaPlayer.create(this, R.raw.sound);

         timer = new CountDownTimer(  300*1000,10000) {
             @Override
             public void onTick(long l) {
                 mp.start();
             }

             @Override
             public void onFinish() {
             }
         }.start();

         databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCES, MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey", "");
        User user = databaseHelper.getUserByUsername(username);

        if (user != null) {

            if (user.getFirstName() != null) {
                firstName.setText(firstName.getText()  + user.getFirstName());
                lastName.setText(lastName.getText() + user.getLastName());
                age.setText(age.getText() + String.valueOf(user.getAge()));
                personalInfo.setText(personalInfo.getText() + user.getPersonalInfo());
            }

            if (user.getContacts().size() > 0){
                contact1.setText(contact1.getText() + user.getContacts().get(0));
                contact2.setText(contact2.getText() + user.getContacts().get(1));

            }

        }
    }

    public void onBackPressed(){

        if (mp.isPlaying()){
            mp.stop();
            timer.cancel();
        }

        Intent intent = new Intent( SosPage.this,MainPage.class);
        startActivity(intent);
    }

}
