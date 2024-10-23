package com.example.taskmanagement.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.taskmanagement.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "TaskManagement.db";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_USERNAME + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_USER_TABLE);
            onCreate(db);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error upgrading database", e);
        }
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_USERNAME, user.getUsername());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());

            db.insert(TABLE_USER, null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding user", e);
        } finally {
            db.close();
        }
    }

    public boolean checkUser(String username) {
        String[] columns = { COLUMN_USER_USERNAME };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = null;
        boolean exists = false;
        try {
            cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
            exists = cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking user", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return exists;
    }

    public boolean isLoginValid(String username, String password) {
        String[] columns = { COLUMN_USER_USERNAME };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = null;
        boolean isValid = false;
        try {
            cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
            isValid = cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error validating login", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return isValid;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            String[] columns = {COLUMN_USER_NAME, COLUMN_USER_USERNAME, COLUMN_USER_PASSWORD};
            String selection = COLUMN_USER_USERNAME + " = ?";
            String[] selectionArgs = {username};

            // Query the user table
            cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

            // If a record is found, create a User object
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String userUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD));

                // Create a new User object
                user = new User(name, userUsername, password);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching user by username", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user; // Return the User object (or null if not found)
    }

}
