package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.HashingUtils;
import com.example.user.stillwalk.helperclasses.UserData;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginPage extends AppCompatActivity {

    public static final String MyPREFERENCES = "LoginInfo";
    //    private UserData userData = new UserData();
    private TextView signInText;
    private TextView signUpText;
    private EditText username;
    private EditText password;
    private String usernameText;
    private Button login;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signInText = findViewById(R.id.sign_text);
        signUpText = findViewById(R.id.register);
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        Typeface face;

        face = Typeface.createFromAsset(getAssets(), "font/font.otf");

        signInText.setTypeface(face);
        signUpText.setTypeface(face);
        signUpText.setPaintFlags(signUpText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (!TextUtils.isEmpty(usernameText = sharedPreferences.getString("usernameKey", ""))) {

            Intent intent = new Intent(this, MainPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("username", usernameText);
            this.startActivity(intent);
        } else {

            if (!IntroPage.isShowed) {

                Intent intent = new Intent(this, IntroPage.class);
                startActivity(intent);
            }
        }
    }


    public void registerClick(View view) {

        startActivity(new Intent(LoginPage.this, Register.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void loginClick(View view) {

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        if (!TextUtils.isEmpty(usernameText) && !TextUtils.isEmpty(passwordText)) {

            ParseUser.logInInBackground(usernameText, passwordText, (user, e) -> {
                if (user != null){
                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("username", usernameText);
                    databaseHelper.insertUsername(usernameText);

                    saveDataToInternalStorage();
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(LoginPage.this, "Fill the empty boxes", Toast.LENGTH_LONG).show();

        }

    }

    public void saveDataToInternalStorage() {


        String usernameKey = username.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("usernameKey", usernameKey);

        editor.apply();

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(login.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0);


    }

    public void onBackPressed() {
    }



}
