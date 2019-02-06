package com.example.user.stillwalk.classes;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.GPS;
import com.example.user.stillwalk.helperclasses.SMS;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;

import java.util.ArrayList;


public class SmsPage extends AppCompatActivity {

    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 10;

    private UserData userData;
    private User user;

    private Button otherReasonB;
    private Button tensionB;
    private Button dizzinessB;
    private Button brokenBonesB;
    private Button hearAttackB;
    private TextView myLocation;
    public static TextView loadingText;
    public static ProgressBar progressBar;
    public static ArrayList<Button> sosButtons;


    private String username;
    private GPS gps;
    private SMS sms;
    public Location location;
    public static final String MyPREFERENCES = "ContactsInfo" ;
    public SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms);

        otherReasonB = findViewById(R.id.other_reason);
        tensionB = findViewById(R.id.tension_send);
        dizzinessB = findViewById(R.id.dizziness_send);
        brokenBonesB = findViewById(R.id.broken_bones_send);
        hearAttackB = findViewById(R.id.heart_attack_send);

        progressBar = findViewById(R.id.loadingPanel);
        loadingText = findViewById(R.id.loading_textView);
        myLocation = findViewById(R.id.my_location);

        gps = new GPS(this);
        sms = new SMS(this);

        sosButtons = new ArrayList<Button>(){{add(otherReasonB);
        add(tensionB); add(dizzinessB);
        add(brokenBonesB); add(hearAttackB);}};

        gps.getLocation();
        userData = new UserData();
        username = getIntent().getStringExtra("username");
        user = new User();
        getUser();


        if (!GPS.isCheckForPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        }
        if (!SMS.isCheckForPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }



    }

    public void otherReasonSos(View view){

        sendMessage(" OTHER REASON SOS BUTTON WAS CLICKED.");
    }

    public void tensionSos(View view){
        sendMessage(" TENSION SOS BUTTON WAS CLICKED.");
    }

    public void heartAttackSos(View v){
        sendMessage(" HEART ATTACK SOS BUTTON WAS CLICKED.");

    }
    public void dizzinessSos(View v){
        sendMessage(" DIZZINESS SOS BUTTON WAS CLICKED.");

    }

    public void brokenBonesSos(View v){
        sendMessage(" BROKEN BONES SOS BUTTON WAS CLICKED.");

    }


    public void sendMessage(String s){

        location = gps.getLocation();

        if (gps.getMy_provider() != null){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (!user.getContacts().isEmpty()) {

            StringBuilder msg = new StringBuilder();

            if(!TextUtils.isEmpty(user.getMessage())){
                msg.append(user.getMessage());
                msg.append(".");
            }

            msg.append(s);
            msg.append(" The exact coordinates are: ");
            msg.append(location.getLatitude());
            msg.append(",");
            msg.append(location.getLongitude());


            if (sms.sendMessage(user.getContacts().get(0), msg.toString()) ) {
                Toast.makeText(SmsPage.this, "send successfully", Toast.LENGTH_SHORT).show();
            }
            if (sms.sendMessage(user.getContacts().get(1), msg.toString())){
                Toast.makeText(SmsPage.this, "send successfully", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(SmsPage.this, "error", Toast.LENGTH_SHORT).show();

            }
        }
    }



    public User getUser() {

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        if (!TextUtils.isEmpty(sharedPreferences.getString("phoneNumb1Key", ""))
                && sharedPreferences.getString("usernameKey", "").equals(username)) {

            String phoneNumber1 = sharedPreferences.getString("phoneNumb1Key", "");
            String phoneNumber2 = sharedPreferences.getString("phoneNumb2Key", "");

            user.setContacts(new ArrayList<String>() {{
                add(phoneNumber1);
                add(phoneNumber2);
            }});
            user.setMessage(sharedPreferences.getString("messageKey", ""));

        } else {
            new Thread(() -> user = userData.getContacts(username)).start();
        }
        return user;

    }


    //Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case SEND_SMS_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else {
                    Toast.makeText(SmsPage.this,"Permission denied",Toast.LENGTH_SHORT).show();

                }

                break;
            }

        }
    }

    public static ArrayList<Button> getSosButtons() {
        return sosButtons;
    }
}
