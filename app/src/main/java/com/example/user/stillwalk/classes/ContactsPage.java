package com.example.user.stillwalk.classes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.stillwalk.R;

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
    public static final String MyPREFERENCES = "ContactsInfo" ;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_page);


        phoneNumber1 = findViewById(R.id.phone1_id);
        phoneNumber2 = findViewById(R.id.phone2_id);
        message = findViewById(R.id.messsage_id);

        username = getIntent().getStringExtra("username");
        userData = new UserData();
        handler = new Handler(Looper.getMainLooper());

        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        user = new User();
        user.setUsername(username);

        if (!TextUtils.isEmpty(sharedPreferences.getString("usernameKey",""))
                &&  sharedPreferences.getString("usernameKey","").equals(username)){

            phoneNumber1.setText(sharedPreferences.getString("phoneNumb1Key",""));

            phoneNumber2.setText(sharedPreferences.getString("phoneNumb2Key",""));
            message.setText(sharedPreferences.getString("messageKey",""));


        }else {

            new Thread(() -> {
                user = userData.getContacts(username);
                handler.post(() -> {

                    // Code here will run in UI thread
                    //Each Handler instance is associated with a single thread and that thread's message queue.
                    // When you create a new Handler, it is bound to the thread / message queue of the thread that is creating it
                    ArrayList<String> contacts = user.getContacts();
                    phoneNumber1.setText(contacts.get(0));
                    phoneNumber2.setText(contacts.get(1));

                    message.setText(user.getMessage());

                    saveDataToInternalStorage();

                });
                user.setUsername(username);

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

               boolean check =  userData.addContacts(user);

                handler.post(()->{

                    if (check){
                        Toast.makeText(this,"Data is saved",Toast.LENGTH_LONG).show();
                        saveDataToInternalStorage();

                    }else {
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();

                    }

                });
            }).start();

        }else {
            Toast.makeText(this,"Please fill all fields!",Toast.LENGTH_LONG).show();
        }

    }
    public void saveDataToInternalStorage(){


        String contact1 = phoneNumber1.getText().toString();
        String contact2 = phoneNumber2.getText().toString();
        String messageString = message.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("phoneNumb1Key",contact1);
        editor.putString("phoneNumb2Key",contact2);
        editor.putString("messageKey",messageString);
        editor.putString("usernameKey",username);

        editor.apply();

    }
}
