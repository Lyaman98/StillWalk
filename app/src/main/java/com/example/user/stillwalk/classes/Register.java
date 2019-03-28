package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.HashingUtils;
import com.example.user.stillwalk.helperclasses.UserData;
import com.example.user.stillwalk.helperclasses.Validation;


public class Register extends AppCompatActivity {

    private UserData userData;
    private Handler handler;
    private TextView userName;
    private TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        userName = findViewById(R.id.username);
        password =findViewById(R.id.password);
        TextView sign_text = findViewById(R.id.sign_text);
        TextView sign_in = findViewById(R.id.sign_in);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/font.otf");
        sign_text.setTypeface(typeface);
        sign_in.setTypeface(typeface);
        sign_in.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        userData = new UserData();
        handler = new Handler();

    }

    public void saveContact(View view){

        String username = userName.getText().toString();
        String pass = password.getText().toString();

        if (!Validation.validateEmail(username)){
            Toast.makeText(this,"Incorrect email",Toast.LENGTH_LONG).show();
            return;
        }
        if (!Validation.validatePassword(pass)){
            Toast.makeText(this,"Password must be more than 5 characters",Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(() -> {
            boolean check = userData.registerUser(username, HashingUtils.hashPassowrd(pass));
            handler.post(() -> {
                if (check) {
                    Toast.makeText(this, "User registered", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show();
                }
            });
        }).start();

    }

    public void signInClick(View view){
        Intent intent = new Intent(getApplicationContext(),LoginPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void onBackPressed(){}
}
