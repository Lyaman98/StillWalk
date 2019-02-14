package com.example.user.stillwalk.classes;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class SmsPage extends AppCompatActivity {

    private UserData userData;
    private User user;

    private Button otherReasonB;
    private Button tensionB;
    private Button dizzinessB;
    private Button hearAttackB;
    public TextView loadingText;
    public ProgressBar progressBar;


    private String username;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location myLocation;
    public static final String MyPREFERENCES = "ContactsInfo";
    public static final String USERNAME_PREFERENCE = "LoginInfo";

    public SharedPreferences sharedPreferences;
    private boolean haveContacts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.send_sms);

        otherReasonB = findViewById(R.id.other_reason);
        tensionB = findViewById(R.id.tension_send);
        dizzinessB = findViewById(R.id.dizziness_send);
        hearAttackB = findViewById(R.id.heart_attack_send);

        progressBar = findViewById(R.id.loadingPanel);
        loadingText = findViewById(R.id.loading_textView);

        checkLocation();

        userData = new UserData();
        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCE,MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey","");
        user = new User();
        getUser();


    }



    private void checkLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation = location;
                if (haveContacts){
                    showButtons();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.SEND_SMS)){

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void otherReasonSos(View view) {

        sendMessage("SOS BUTTON WAS CLICKED.");
    }

    public void tensionSos(View view) {
        sendMessage(" TENSION SOS BUTTON WAS CLICKED.");
    }

    public void heartAttackSos(View v) {
        sendMessage(" HEART ATTACK SOS BUTTON WAS CLICKED.");

    }

    public void dizzinessSos(View v) {
        sendMessage(" DIZZINESS SOS BUTTON WAS CLICKED.");

    }


    public void sendMessage(String s) {

        if (!user.getContacts().isEmpty()) {


            StringBuilder msg = new StringBuilder();

            if (!TextUtils.isEmpty(user.getMessage())) {
                msg.append(user.getMessage());
                msg.append(".");
            }

            msg.append(s);
            msg.append(" The exact location is : ");

            try {
                String googleMaps = "https://www.google.com/maps/place/";
                msg.append(new URL(googleMaps + myLocation.getLatitude() + "," + myLocation.getLongitude()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (sendMessage(user.getContacts().get(0), msg.toString())) {
                Toast.makeText(SmsPage.this, "send successfully", Toast.LENGTH_SHORT).show();
            }
            if (sendMessage(user.getContacts().get(1), msg.toString())) {
                Toast.makeText(SmsPage.this, "send successfully", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(SmsPage.this, "error", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private boolean sendMessage(String phoneNumber, String message) {

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(message)) {

            if (checkPermission(Manifest.permission.SEND_SMS) && checkPermission(Manifest.permission.READ_PHONE_STATE)) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                return true;
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Enter phone number and message", Toast.LENGTH_SHORT).show();
        }
        return false;
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
            haveContacts = true;


        } else {
            new Thread(() -> {

                if (userData.getContacts(username) != null) {
                    user = userData.getContacts(username);
                    haveContacts = true;

                }
            }).start();
        }
        return user;

    }

    private void showButtons(){

        if (user.getContacts() != null){
            progressBar.setVisibility(View.INVISIBLE);
            loadingText.setVisibility(View.INVISIBLE);
            otherReasonB.setVisibility(View.VISIBLE);
            tensionB.setVisibility(View.VISIBLE);
            dizzinessB.setVisibility(View.VISIBLE);
            hearAttackB.setVisibility(View.VISIBLE);
        }
    }


    //Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        for (String permission : permissions){

            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && requestCode == 1){
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                    }
                break;
            }
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);

        return check == PackageManager.PERMISSION_GRANTED;
    }

}
