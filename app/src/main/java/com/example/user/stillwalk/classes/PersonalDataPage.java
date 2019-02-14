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
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "PersonalInfo" ;
    public static final String USERNAME_PREFERENCES = "LoginInfo" ;


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

        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCES,MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey","");

        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        user = new User();
        user.setUsername(username);

        if (!TextUtils.isEmpty(sharedPreferences.getString("firstNameKey",""))
                && sharedPreferences.getString("usernameKey","").equals(username)){

            firstName.setText(sharedPreferences.getString("firstNameKey",""));

            lastName.setText(sharedPreferences.getString("lastNameKey",""));
            age.setText(String.valueOf(sharedPreferences.getInt("ageKey",0)));
            personal_info.setText(sharedPreferences.getString("personalInfoKey",""));
        }else {

            new Thread(() -> {
                user = userData.getUserData(username);
                handler.post(() -> {

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    age.setText(String.valueOf(user.getAge()));
                    personal_info.setText(user.getPersonalInfo());
                    saveDataToInternalStorage();
                });
                user.setUsername(username);
            }).start();
        }
    }

    public void saveUserData(View view){

                if (!isNetworkAvailable()){
                    Toast.makeText(this,"Please connect to the internet..",Toast.LENGTH_LONG).show();
                    return;
                }

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
                                saveDataToInternalStorage();

                            }else {
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();

                            }

                        });
                    }).start();

                } else {
                    Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }

        }
        public void saveDataToInternalStorage(){

            String firstNameString = firstName.getText().toString();
            String lastNameString = lastName.getText().toString();
            String personalInfoString = personal_info.getText().toString();
            int ageInt = Integer.parseInt(age.getText().toString());

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("firstNameKey",firstNameString);
            editor.putString("lastNameKey",lastNameString);
            editor.putString("personalInfoKey",personalInfoString);
            editor.putInt("ageKey",ageInt);
            editor.putString("usernameKey",username);
            editor.apply();


        }

        //??????????????????????????

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

