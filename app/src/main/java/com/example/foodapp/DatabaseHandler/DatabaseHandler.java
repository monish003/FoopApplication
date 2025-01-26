package com.example.foodapp.DatabaseHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_app.db";
    private static final int DATABASE_VERSION = 1;

    // Table User
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_Phone = "phone";
    private static final String KEY_DOB = "dob";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_USERNAME + " TEXT,"
                    + KEY_EMAIL + " TEXT,"
                    + KEY_Phone + " TEXT,"
                    + KEY_DOB + " TEXT,"
                    + KEY_PASSWORD + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String username, String email, String password, String phone, String DOB) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_Phone, phone);
        values.put(KEY_DOB, DOB);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID}, KEY_EMAIL + "=?", new String[]{email}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public Cursor fetchUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, KEY_USERNAME + "=?", new String[]{username}, null, null, null);
    }

    public boolean validateUser(String username, String password) {
        Cursor cursor = fetchUserByUsername(username);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String dbPassword = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            cursor.close();
            return dbPassword.equals(password);
        }
        return false;
    }

    // Retrieve Latest Username
    public String getLatestUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_USERNAME + " FROM " + TABLE_USERS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
            cursor.close();
            return username;
        }
        return null;
    }

    // Retrieve Latest Email
    public String getLatestEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_EMAIL + " FROM " + TABLE_USERS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
            cursor.close();
            return email;
        }
        return null;
    }

    // Retrieve Latest Phone
    public String getLatestPhone() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_Phone + " FROM " + TABLE_USERS + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(KEY_Phone));
            cursor.close();
            return phone;
        }
        return null;
    }


}
