package com.example.user.stillwalk.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.stillwalk.R;

import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;


public class PersonalDataPage extends AppCompatActivity {


    private EditText firstName;
    private EditText lastName;
    private EditText personal_info;
    private EditText age;

    private UserData userData = new UserData();
    private User user;
    private Handler handler;
    DatabaseHelper databaseHelper;
    private SharedPreferences usernamePreference;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        personal_info = findViewById(R.id.personal_info);
        age = findViewById(R.id.age);
        handler = new Handler();
        databaseHelper = new DatabaseHelper(this);


        usernamePreference = getSharedPreferences("LoginInfo",MODE_PRIVATE);
        user = databaseHelper.getUserByUsername(usernamePreference.getString("usernameKey",""));
        username = user.getUsername();

        if (user != null && user.getUsername() != null && user.getLastName() != null){

            firstName.setText(user.getFirstName());

            lastName.setText(user.getLastName());
            age.setText(String.valueOf(user.getAge()));
            personal_info.setText(user.getPersonalInfo());

        }else {

            new Thread(() -> {


                user = userData.getUserData(username);

                handler.post(() -> {

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    age.setText(String.valueOf(user.getAge()));
                    personal_info.setText(user.getPersonalInfo());

                    databaseHelper.updatePersonalData(user);
                });
            }).start();
        }
    }

    public void saveUserData(View view){


                if (age.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                    return;

                }


                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String personalInfoString = personal_info.getText().toString();
                int ageInt = Integer.parseInt(age.getText().toString());

                if (!TextUtils.isEmpty(firstNameString) && !TextUtils.isEmpty(lastNameString) &&
                        !TextUtils.isEmpty(personalInfoString) && ageInt != 0) {



                    user.setFirstName(firstNameString);
                    user.setLastName(lastNameString);
                    user.setPersonalInfo(personalInfoString);
                    user.setAge(ageInt);

                    new Thread(()-> {

                        boolean check =  userData.addUserData(user);

                        handler.post(()->{

                            if (check){
                                Toast.makeText(this,"Data is saved",Toast.LENGTH_LONG).show();
                                databaseHelper.updatePersonalData(user);

                            }else {
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();

                            }

                        });
                    }).start();

                } else {
                    Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }

        }

}

