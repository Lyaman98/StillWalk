package com.example.user.stillwalk.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.stillwalk.R;

import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;

import java.util.ArrayList;


public class PersonalDataPage extends AppCompatActivity {


    private EditText firstName;
    private EditText lastName;
    private EditText personal_info;
    private Spinner age;
    private Spinner bloodType;

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
        bloodType = findViewById(R.id.bloodType);
        handler = new Handler();
        databaseHelper = new DatabaseHelper(this);


        usernamePreference = getSharedPreferences("LoginInfo",MODE_PRIVATE);
        user = databaseHelper.getUserByUsername(usernamePreference.getString("usernameKey",""));
        username = user.getUsername();

        setAgeList();
        setBloodTypeList();

        if (user != null && user.getUsername() != null && user.getLastName() != null){

            firstName.setText(user.getFirstName());

            lastName.setText(user.getLastName());
            age.setSelection(user.getAge() - 5);
            personal_info.setText(user.getPersonalInfo());

        }else {

            new Thread(() -> {


                user = userData.getUserData(username);

                handler.post(() -> {

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    age.setSelection(user.getAge()- 5);
                    personal_info.setText(user.getPersonalInfo());

                    databaseHelper.updatePersonalData(user);
                });
            }).start();
        }
    }

    public void saveUserData(View view){

                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String personalInfoString = personal_info.getText().toString();
                int ageInt = Integer.parseInt(age.getSelectedItem().toString());

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

        public void setAgeList(){

            ArrayList<Integer> ageList = new ArrayList<>();
            for (int i = 5; i < 100; i++){
                ageList.add(i);
            }
            ArrayAdapter<Integer> listofAges = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,ageList);
            age.setAdapter(listofAges);
        }

        public void setBloodTypeList(){

            ArrayList<String> bloodTypes = new ArrayList<>();
            bloodTypes.add("1(+)");
            bloodTypes.add("1(-)");
            bloodTypes.add("2(+)");
            bloodTypes.add("2(-)");
            bloodTypes.add("3(+)");
            bloodTypes.add("3(-)");
            bloodTypes.add("4(+)");
            bloodTypes.add("4(-)");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,bloodTypes);
            bloodType.setAdapter(arrayAdapter);

        }

        public void onBackPressed(){
            Intent intent = new Intent(PersonalDataPage.this,MainPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }

}

