package com.example.user.stillwalk.classes;

import android.Manifest;
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
    public TextView loadingText;
    public ProgressBar progressBar;


    private String username;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location myLocation;
    public static final String MyPREFERENCES = "ContactsInfo";
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

        checkLocation();

        userData = new UserData();
        username = getIntent().getStringExtra("username");
        user = new User();

        getUser();


    }

    private void checkLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                myLocation = location;
                progressBar.setVisibility(View.INVISIBLE);
                loadingText.setVisibility(View.INVISIBLE);
                otherReasonB.setVisibility(View.VISIBLE);
                tensionB.setVisibility(View.VISIBLE);
                dizzinessB.setVisibility(View.VISIBLE);
                hearAttackB.setVisibility(View.VISIBLE);
                brokenBonesB.setVisibility(View.VISIBLE);

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


        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);

        } else {

            if (!checkPermission(Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.SEND_SMS
                }, SEND_SMS_PERMISSION_REQUEST_CODE);
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }


    public void otherReasonSos(View view) {

        sendMessage(" OTHER REASON SOS BUTTON WAS CLICKED.");
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

    public void brokenBonesSos(View v) {
        sendMessage(" BROKEN BONES SOS BUTTON WAS CLICKED.");

    }


    public void sendMessage(String s) {

        if (!user.getContacts().isEmpty()) {

            Log.i("MYTAG", "" + " ..." + user.getContacts());

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

            if (checkPermission(Manifest.permission.SEND_SMS)) {

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

        } else {
            new Thread(() -> user = userData.getContacts(username)).start();
        }
        return user;

    }


    //Callback for the result from requesting permissions. This method is invoked for every call on requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case SEND_SMS_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(SmsPage.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                        if (!checkPermission(Manifest.permission.SEND_SMS)) {
                            ActivityCompat.requestPermissions(this, new String[]{
                                    Manifest.permission.SEND_SMS
                            }, SEND_SMS_PERMISSION_REQUEST_CODE);

                        }
                        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
                    }
                } else {
                    Toast.makeText(SmsPage.this, "Permission denied", Toast.LENGTH_SHORT).show();

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
