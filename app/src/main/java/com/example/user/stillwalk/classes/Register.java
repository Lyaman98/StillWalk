package com.example.user.stillwalk.classes;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.UserData;


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

        userData = new UserData();
        handler = new Handler();

    }

    public void saveContact(View view){

        String username = userName.getText().toString();
        String pass = password.getText().toString();

        new Thread(()->{

            boolean check = userData.registerUser(username,pass);

            handler.post(()->{

                if (check){
                    Toast.makeText(this,"User registered",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();

                }


            });

        }).start();
    }
}
