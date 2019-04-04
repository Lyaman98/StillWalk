package com.example.user.stillwalk.helperclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static  int DATABASE_VERSION = 7;
    public static final String TABLE_NAME = "userdata";
    public static final String TABLE_NAME2 = "userlocation";
    public static final String COL_1 = "blood_type";
    public static final String COL_2 = "username";
    public static final String COL_3 = "age";
    public static final String COL_4 = "firstName";
    public static final String COL_5 = "lastName";
    public static final String COL_6 = "personal_info";
    public static final String COL_7 = "contact1";
    public static final String COL_8 = "contact2";
    public static final String COL_9 = "message";
    public static final String COL_10 = "latitude";
    public static final String COL_11 = "longitude";


    private static final String CREATE_TABLE = "create table userdata(\n" +
            "\tusername varchar(50) primary key unique ,\n" +
            "    firstName varchar(256),\n" +
            "    lastName varchar(256),\n" +
            "    personal_info text,\n" +
            "    contact1 varchar(256),\n" +
            "    contact2 varchar(256),\n" +
            "    message text,\n" +
            "    age int,\n" +
            "    blood_type varchar(5)\n" +
            "    );";

    private static final String CREATE_TABLE2 = "create table userlocation(\n" +
            "    username varchar(256) primary key ,\n" +
            "    latitude text,\n" +
            "    longitude text\n" +
            "    );";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    public  User getUserByUsername(String username){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_1,COL_3,COL_4,COL_5,COL_6,COL_7,COL_8,COL_9},
                COL_2  + "=?", new String[]{username},null,null,null);

        User user = null;

        if (cursor.moveToFirst()){
            cursor.moveToFirst();

            ArrayList<String> contacts = new ArrayList<>(Arrays.asList(
                    cursor.getString(cursor.getColumnIndex(COL_7)),
                    cursor.getString(cursor.getColumnIndex(COL_8))

                    ));

             user = new User(username,
                    cursor.getInt(cursor.getColumnIndex(COL_3)),
                    cursor.getString(cursor.getColumnIndex(COL_4)),
                    cursor.getString(cursor.getColumnIndex(COL_5)),
                    cursor.getString(cursor.getColumnIndex(COL_6)),
                    contacts,
                    cursor.getString(cursor.getColumnIndex(COL_9)),
                    cursor.getString(cursor.getColumnIndex(COL_1))

            );

            cursor.close();

        }

        return user;

    }


    public  ArrayList<String> getLocation(String username){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME2, new String[]{COL_10,COL_11},
                COL_2  + "=?", new String[]{username},null,null,null);

        ArrayList<String> location = null;

        if (cursor.moveToFirst()){
            location = new ArrayList<>(Arrays.asList(
                    cursor.getString(cursor.getColumnIndex(COL_10)),
                    cursor.getString(cursor.getColumnIndex(COL_11))
            ));
        }
        return location;

    }


    public void insertUsername(String usernameValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues username = new ContentValues();
        username.put(COL_2,usernameValue);

        if ( getUserByUsername(usernameValue) == null) {

            db.insert(TABLE_NAME, null, username);
        }

        if (getLocation(usernameValue) == null){
            db.insert(TABLE_NAME2, null, username);
        }

    }

    public void updatePersonalData(User user){

        SQLiteDatabase db = this.getWritableDatabase();

        if (user.getFirstName() != null) {

            db.execSQL("update  " + TABLE_NAME + " set " +
                    COL_4 + " = '" + user.getFirstName() + "'," +
                    COL_5 + " = '" + user.getLastName() + "'," +
                    COL_6 + " = '" + user.getPersonalInfo() + "'," +
                    COL_3 + " = '" + user.getAge() + "' ," +
                    COL_1 + " ='" + user.getBloodType() + "' " +
                    "where username = '" + user.getUsername() + "'");
        }

    }

    public void updateContacts(User user){

        SQLiteDatabase db = this.getWritableDatabase();


        if(user.getContacts().get(0) != null) {

            db.execSQL("update  " + TABLE_NAME + " set " +
                    COL_7 + " = '" + user.getContacts().get(0) + "'," +
                    COL_8 + " = '" + user.getContacts().get(1) + "'," +
                    COL_9 + " = '" + user.getMessage() + "' " +
                    "where username = '" + user.getUsername() + "'");
        }

    }

    public void updateLocation(String username,Double latitude, Double longitude){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("update  " + TABLE_NAME2 + " set " +
                COL_10 + " = '" + Double.toString(latitude) + "'," +
                COL_11 + " = '" + Double.toString(longitude) + "' " +
                "where username = '" + username + "'");

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME2);

    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
