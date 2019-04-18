package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class PersonalDataPage extends AppCompatActivity {


    private EditText firstName;
    private EditText lastName;
    private EditText personal_info;
    private Spinner age;
    private Spinner bloodType;
    private User user;
    DatabaseHelper databaseHelper;
    private SharedPreferences usernamePreference;
    private ArrayList<String> bloodTypes;

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
        databaseHelper = new DatabaseHelper(this);
        usernamePreference = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        user = databaseHelper.getUserByUsername(usernamePreference.getString("usernameKey", ""));
        username = user.getUsername();


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> {
            databaseHelper.close();
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        });

        setAgeList();
        setBloodTypeList();

        if (user == null || user.getUsername() == null || user.getLastName() == null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.setLimit(1);

            query.findInBackground((objects, e) -> {
                if (e == null){

                    if (objects.size() > 0){

                        ParseObject object = objects.get(0);
                        String firstNameString = object.getString("firstName");
                        String lastNameString = object.getString("lastName");
                        int ageInt = object.getInt("age") - 5;
                        String personalInfo = object.getString("personalInfo");
                        String bloodTypeString = object.getString("bloodType");
                        user = new User(username, ageInt, firstNameString,lastNameString,personalInfo,bloodTypeString);
                        databaseHelper.updatePersonalData(user);

                        setFields(user);
                    }
                }
            });
        }else {
            setFields(user);
        }


    }

    private void setFields(User user){

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        age.setSelection(user.getAge() - 5);
        personal_info.setText(user.getPersonalInfo());
        bloodType.setSelection(bloodTypes.indexOf(user.getBloodType()));
    }

    public void saveUserData(View view) {

        String firstNameString = firstName.getText().toString();
        String lastNameString = lastName.getText().toString();
        String personalInfoString = personal_info.getText().toString();
        int ageInt = Integer.parseInt(age.getSelectedItem().toString());
        String bloodTypeValue = bloodType.getSelectedItem().toString();

        if (!TextUtils.isEmpty(firstNameString) && !TextUtils.isEmpty(lastNameString) &&
                !TextUtils.isEmpty(personalInfoString) && ageInt != 0) {



            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.setLimit(1);

            query.findInBackground((objects, e) -> {
                if (e == null){

                    ParseObject userdata;

                    if (objects.size() > 0){

                        userdata = objects.get(0);

                    }else {
                        userdata = new ParseObject("UserData");
                    }
                    userdata.put("firstName",firstNameString);
                    userdata.put("lastName",lastNameString);
                    userdata.put("personalInfo",personalInfoString);
                    userdata.put("age", ageInt);
                    userdata.put("bloodType",bloodTypeValue);
                    userdata.put("username",username);
                    userdata.saveInBackground();
                    user = new User(username,ageInt,firstNameString,lastNameString,personalInfoString,bloodTypeValue);
                    databaseHelper.updatePersonalData(user);

                    Toast.makeText(this,"Data is saved",Toast.LENGTH_LONG).show();

                }
            });


        } else {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }

    }

    public void setAgeList() {

        ArrayList<Integer> ageList = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            ageList.add(i);
        }
        ArrayAdapter<Integer> listofAges = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, ageList);
        age.setAdapter(listofAges);
    }

    public void setBloodTypeList() {

        bloodTypes = new ArrayList<>();
        bloodTypes.add("1(+)");
        bloodTypes.add("1(-)");
        bloodTypes.add("2(+)");
        bloodTypes.add("2(-)");
        bloodTypes.add("3(+)");
        bloodTypes.add("3(-)");
        bloodTypes.add("4(+)");
        bloodTypes.add("4(-)");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, bloodTypes);
        bloodType.setAdapter(arrayAdapter);

    }

    public void hideKeyboard(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(firstName.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(lastName.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(personal_info.getWindowToken(), 0);


    }
    public void onBackPressed() {
    }

}

