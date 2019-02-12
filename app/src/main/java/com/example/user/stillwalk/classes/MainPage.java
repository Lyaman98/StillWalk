package com.example.user.stillwalk.classes;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.stillwalk.R;

public class MainPage extends AppCompatActivity {


    public static final String MyPREFERENCES = "LoginInfo" ;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button personalInfo = findViewById(R.id.personal_info);
        Button contacts = findViewById(R.id.contacts);
        Button sos = findViewById(R.id.sos);
        Button signOut = findViewById(R.id.sign_out);

        String username = getIntent().getStringExtra("username");

        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, PersonalDataPage.class);
            intent.putExtra("username",username);
            startActivity(intent);
        });
        sos.setOnClickListener(v -> {

            //Context – это объект, который предоставляет доступ к базовым функциям приложения:
            // доступ к ресурсам, к файловой системе, вызов активности и т.д. Activity является подклассом Context,
            // поэтому в коде мы можем использовать её как ИмяАктивности.this (напр. MainActivity.this), или укороченную запись this.
            // Классы Service, Application и др. также работают с контекстом.

                Intent intent = new Intent(MainPage.this, SmsPage.class);
                intent.putExtra("username",username);
                startActivity(intent);
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, ContactsPage.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("usernameKey","");

                editor.apply();
                startActivity(new Intent(MainPage.this,LoginPage.class));
            }
        });



    }

    @Override
    public void onBackPressed() {
    }
}
