package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactsPage extends AppCompatActivity {

    private User user;
    private EditText phoneNumber1;
    private EditText phoneNumber2;
    private EditText message;
    private String username;
    public static final String USERNAME_PREFERENCE = "LoginInfo";
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_page);


        phoneNumber1 = findViewById(R.id.phone1_id);
        phoneNumber2 = findViewById(R.id.phone2_id);
        message = findViewById(R.id.messsage_id);
        databaseHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCE, MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey", "");

        user = databaseHelper.getUserByUsername(username);


        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> {
            databaseHelper.close();
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        });

        if (user != null && user.getUsername() != null && user.getContacts().get(0) != null) {
            setFields(user);
        } else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
            query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.setLimit(1);

            query.findInBackground((objects, e) -> {
                if (e == null) {

                    if (objects.size() > 0) {
                        ParseObject object = objects.get(0);

                        String contact1 = object.getString("contact1");
                        String contact2 = object.getString("contact1");
                        String messageString = object.getString("message");

                        ArrayList<String> contacts = new ArrayList<>();
                        contacts.add(contact1);
                        contacts.add(contact2);

                        user.setContacts(contacts);
                        user.setMessage(messageString);

                        databaseHelper.updateContacts(user);

                        setFields(user);

                    }
                }
            });
        }
    }

    private void setFields(User user){
        phoneNumber1.setText(user.getContacts().get(0));
        phoneNumber2.setText(user.getContacts().get(1));
        message.setText(user.getMessage());
    }

    public void saveContacts(View view) {

        String contact1 = phoneNumber1.getText().toString();
        String contact2 = phoneNumber2.getText().toString();
        String messageString = message.getText().toString();

        if (!TextUtils.isEmpty(contact1) && !TextUtils.isEmpty(contact2)) {

            ArrayList<String> contacts = new ArrayList<>();
            contacts.add(contact1);
            contacts.add(contact2);

            user.setContacts(contacts);
            user.setMessage(messageString);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.setLimit(1);

            query.findInBackground((objects, e) -> {
                if (e == null) {
                    if (objects.size() > 0) {

                        ParseObject userdata = objects.get(0);
                        userdata.put("contact1",contact1);
                        userdata.put("contact2",contact2);
                        userdata.put("message",messageString);
                        userdata.saveInBackground();

                        databaseHelper.updateContacts(user);
                        Toast.makeText(this, "Data is saved", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_LONG).show();
        }

    }


    public void onBackPressed() {
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(phoneNumber1.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(phoneNumber2.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(message.getWindowToken(), 0);


    }

}
