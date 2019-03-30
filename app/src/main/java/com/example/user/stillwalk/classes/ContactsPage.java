package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.stillwalk.R;

import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.User;
import com.example.user.stillwalk.helperclasses.UserData;

import java.util.ArrayList;

public class ContactsPage extends AppCompatActivity {

    private UserData userData;
    private User user;
    private Handler handler;
    private EditText phoneNumber1;
    private EditText phoneNumber2;
    private EditText message;
    private String username;
    public static final String USERNAME_PREFERENCE = "LoginInfo" ;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_page);


        phoneNumber1 = findViewById(R.id.phone1_id);
        phoneNumber2 = findViewById(R.id.phone2_id);
        message = findViewById(R.id.messsage_id);
        userData = new UserData();
        handler = new Handler(Looper.getMainLooper());
        databaseHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences(USERNAME_PREFERENCE,MODE_PRIVATE);
        username = sharedPreferences.getString("usernameKey","");

        user = databaseHelper.getUserByUsername(username);

        if (user != null && user.getUsername() != null && user.getContacts().get(0) != null){

            phoneNumber1.setText(user.getContacts().get(0));
            phoneNumber2.setText(user.getContacts().get(1));
            message.setText(user.getMessage());


        }else {

            new Thread(() -> {
                user = userData.getContacts(username);

                handler.post(() -> {

                    ArrayList<String> contacts = user.getContacts();
                    phoneNumber1.setText(contacts.get(0));
                    phoneNumber2.setText(contacts.get(1));
                    message.setText(user.getMessage());

                    databaseHelper.updateContacts(user);

                });

            }).start();

        }
    }

    public void saveContacts(View view){

        String contact1 = phoneNumber1.getText().toString();
        String contact2 = phoneNumber2.getText().toString();
        String messageString = message.getText().toString();

        if (!TextUtils.isEmpty(contact1) && !TextUtils.isEmpty(contact2)){

            ArrayList<String> contacts = new ArrayList<>();
            contacts.add(contact1);
            contacts.add(contact2);

            user.setContacts(contacts);
            user.setMessage(messageString);

            new Thread(()-> {

                userData.addContacts(user);

                handler.post(()->{
                    databaseHelper.updateContacts(user);
                    Toast.makeText(this,"Data is saved", Toast.LENGTH_LONG).show();
                });
            }).start();

        }else {
            Toast.makeText(this,"Please fill all fields!",Toast.LENGTH_LONG).show();
        }

    }


    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(phoneNumber1.getWindowToken(),0);
        inputMethodManager.hideSoftInputFromWindow(phoneNumber2.getWindowToken(),0);
        inputMethodManager.hideSoftInputFromWindow(message.getWindowToken(),0);


    }
    public void onBackPressed(){
        databaseHelper.close();
        Intent intent = new Intent(this,MainPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

}
