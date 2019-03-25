package com.example.user.stillwalk.classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.stillwalk.R;
import com.example.user.stillwalk.helperclasses.DatabaseHelper;
import com.example.user.stillwalk.helperclasses.GetLocationService;
import com.example.user.stillwalk.helperclasses.HashingUtils;
import com.example.user.stillwalk.helperclasses.UserData;

import org.sqlite.SQLiteDataSource;

public class LoginPage extends AppCompatActivity {

//    private UserData userData = new UserData();
    private UserData userData;
    private EditText username;
    private EditText password;
    private Handler handler;
    private String usernameText;
    public static final String MyPREFERENCES = "LoginInfo" ;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        handler = new Handler();
        userData = new UserData();
        sharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        if (!TextUtils.isEmpty(usernameText = sharedPreferences.getString("usernameKey",""))){
            Intent intent = new Intent(this, MainPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("username", usernameText);
            this.startActivity(intent);
        }

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(),0);


    }

    public void registerClick(View view){

        startActivity(new Intent(LoginPage.this,Register.class));

    }
    public void loginClick(View view){

            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();

            if (!TextUtils.isEmpty(usernameText) && !TextUtils.isEmpty(passwordText)) {


                new Thread(() -> {


                    boolean check = userData.checkUser(usernameText, HashingUtils.hashPassowrd(passwordText));

                    handler.post(()->{

                        if (check) {
                            Intent intent = new Intent(this, MainPage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("username", usernameText);
                            databaseHelper = new DatabaseHelper(this);
                            databaseHelper.insertUsername(usernameText);


                            saveDataToInternalStorage();
                            this.startActivity(intent);
                        } else {
                            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_LONG).show();

                        }
                    });

                }).start();


            } else {
                Toast.makeText(LoginPage.this, "Fill the empty boxes", Toast.LENGTH_LONG).show();

            }

        }
    public void saveDataToInternalStorage(){


        String usernameKey = username.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("usernameKey",usernameKey);

        editor.apply();

    }
}
