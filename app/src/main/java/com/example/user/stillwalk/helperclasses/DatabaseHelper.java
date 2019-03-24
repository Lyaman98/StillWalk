package com.example.user.stillwalk.helperclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import org.sqlite.SQLiteDataSource;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "userdata";
    public static final String COL_1 = "id";
    public static final String COL_2 = "username";
    public static final String COL_3 = "age";
    public static final String COL_4 = "firstName";
    public static final String COL_5 = "lastName";
    public static final String COL_6 = "personal_info";
    public static final String COL_7 = "contact1";
    public static final String COL_8 = "contact2";
    public static final String COL_9 = "message";

    public static final String CREATE_TABLE = "create table userdata(\n" +
            "\tusername varchar(50),\n" +
            "    password varchar(50),\n" +
            "    firstName varchar(256),\n" +
            "    lastName varchar(256),\n" +
            "    personal_info text,\n" +
            "    contact1 varchar(256),\n" +
            "    contact2 varchar(256),\n" +
            "    message text,\n" +
            "    age int\n" +
            "    );";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }


    public long insertUsername(String usernameValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues username = new ContentValues();
        username.put(COL_2,usernameValue);

        long id = db.insert(TABLE_NAME,null,username);

        return id;
    }

    public int updateFirstName(User user){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues firstName = new ContentValues();
        firstName.put(COL_4,user.getFirstName());

        return db.update(TABLE_NAME,firstName,COL_1 + " = ?",
                new String[]{String.valueOf(user.getId())});

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
    }
}
