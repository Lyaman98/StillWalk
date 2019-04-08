package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.HashingUtils;
import com.example.user.stillwalk.helperclasses.UserData;
import com.example.user.stillwalk.helperclasses.Validation;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Register extends AppCompatActivity {

    private TextView userName;
    private TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        TextView sign_text = findViewById(R.id.sign_text);
        TextView sign_in = findViewById(R.id.sign_in);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/font.otf");
        sign_text.setTypeface(typeface);
        sign_in.setTypeface(typeface);
        sign_in.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


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
            if(e != null){
                Toast.makeText(getApplicationContext(),"User registered successfully",Toast.LENGTH_LONG).show();
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
}
