package com.example.user.stillwalk.classes;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.GetLocationService;
import com.parse.ParseUser;

public class MainPage extends AppCompatActivity {

    private boolean isInstalled;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "LoginInfo";
    public static final String SHORTCUTP_REFERENCE = "ShortCutInfo";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        CardView personalInfo = findViewById(R.id.personal_info);
        CardView contacts = findViewById(R.id.contacts);
        CardView sos = findViewById(R.id.sos);
        CardView signOut = findViewById(R.id.sign_out);

        sharedPreferences = getSharedPreferences(SHORTCUTP_REFERENCE,MODE_PRIVATE);
        isInstalled = sharedPreferences.getBoolean("isInstalled",false);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Still Walking");

        if (!isInstalled) {
            createShortCut();
            showPopup();
        }

        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, PersonalDataPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        sos.setOnClickListener(v -> {

            Intent intent = new Intent(MainPage.this, SmsPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        contacts.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, ContactsPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        signOut.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.identification3)
                    .setTitle("Log out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {

                        ParseUser.logOut();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isInstalled",false);
                        editor.apply();

                        stopService(new Intent(getApplicationContext(), GetLocationService.class));
                        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("usernameKey", "");
                        editor.apply();


                        startActivity(new Intent(MainPage.this, LoginPage.class));
                    })
                    .setNegativeButton("No", null)
                    .show();

        });

        startService(new Intent(this, GetLocationService.class));
    }


    public void appInfo(View view) {
        Intent intent = new Intent(this, StillWalkInfo.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
    }

    public void createShortCut(){

            Intent shortCut = new Intent(getApplicationContext(), SmsPage.class);
            shortCut.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCut);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "SOS");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.sos));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isInstalled",true);
            editor.apply();



    }

    public void showPopup(){

        new AlertDialog.Builder(this)
                .setMessage("Please click to learn more about the application, before you started to use it ")
                .setTitle("Attention")
                .setPositiveButton("OK",null)
                .show();
    }
}
