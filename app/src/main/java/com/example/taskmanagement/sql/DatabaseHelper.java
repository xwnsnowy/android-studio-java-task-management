package com.example.taskmanagement.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskmanagement.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Khai báo các biến cơ bản cho tên và phiên bản cơ sở dữ liệu
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskManagement.db";

    // Tên bảng và các cột trong bảng
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Câu lệnh SQL để tạo bảng người dùng với cột user_id tự động tăng
    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // Câu lệnh SQL để xóa bảng người dùng
    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    // Constructor của DatabaseHelper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng khi cơ sở dữ liệu được tạo lần đầu
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng người dùng cũ nếu nó tồn tại và tạo lại bảng mới
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    // Thêm người dùng mới vào cơ sở dữ liệu
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Kiểm tra xem người dùng có tồn tại không bằng user name
    public boolean checkUser(String username) {
        String[] columns = { COLUMN_USER_NAME };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    // Kiểm tra xem người dùng có tồn tại không bằng user name và password
    public boolean isLoginValid(String username, String password) {
        String[] columns = { COLUMN_USER_NAME };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
}
