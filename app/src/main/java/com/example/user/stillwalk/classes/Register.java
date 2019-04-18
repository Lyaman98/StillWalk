package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.Validation;
import com.parse.ParseUser;


public class Register extends AppCompatActivity {

    private TextView userName;
    private TextView password;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "LoginInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void saveContact(View view) {

        String username = userName.getText().toString();
        String pass = password.getText().toString();

        if (!Validation.validateEmail(username)) {
            Toast.makeText(this, "Incorrect email", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Validation.validatePassword(pass)) {
            Toast.makeText(this, "Password must be more than 5 characters", Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(pass);
        parseUser.signUpInBackground(e -> {
            if (e == null) {
                Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username", username);
                databaseHelper.insertUsername(username);
                saveDataToInternalStorage(username);

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void signInClick(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(userName.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0);
    }

    public void onBackPressed() {

    }

    public void saveDataToInternalStorage(String usernameKey) {


        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("usernameKey", usernameKey);

        editor.apply();

    }

}
