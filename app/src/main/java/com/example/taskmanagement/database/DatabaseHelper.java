package com.example.taskmanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.taskmanagement.R;
import com.example.taskmanagement.models.State;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "TaskManagement.db";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String TABLE_TASK = "task";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_NAME = "task_name";
    private static final String COLUMN_TASK_DESCRIPTION = "task_description";
    private static final String COLUMN_TASK_ESTIMATE_DURATION = "task_estimate_duration";
    private static final String COLUMN_TASK_CONTEXT = "task_context";
    private static final String COLUMN_TASK_URL = "task_url";
    private static final String COLUMN_TASK_STATE = "task_state";
    private static final String COLUMN_TASK_PROJECT = "task_project";
    private static final String COLUMN_TASK_BEGIN_DATE = "begin_date";
    private static final String COLUMN_TASK_END_DATE = "end_date";

    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_USERNAME + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private final String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK + "("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_NAME + " TEXT, "
            + COLUMN_TASK_DESCRIPTION + " TEXT, "
            + COLUMN_TASK_ESTIMATE_DURATION + " TEXT, "
            + COLUMN_TASK_BEGIN_DATE + " TEXT, "
            + COLUMN_TASK_END_DATE + " TEXT, "
            + COLUMN_TASK_STATE + " TEXT, "
            + COLUMN_TASK_CONTEXT + " TEXT, "
            + COLUMN_TASK_PROJECT + " TEXT, "
            + COLUMN_TASK_URL + " TEXT, "
            + COLUMN_USER_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")"
            + ")";

    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private final String DROP_TASK_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASK;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_TASK_TABLE);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_TASK + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")");
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Error adding column user_id", e);
            }
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

    public void addTask(Task task, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            // Log các giá trị đang được đưa vào ContentValues để kiểm tra
            Log.d("DatabaseHelper", "Task Name: " + task.getName());
            Log.d("DatabaseHelper", "Task Description: " + task.getDescription());
            Log.d("DatabaseHelper", "Task Estimate Duration: " + task.getEstimateDuration());
            Log.d("DatabaseHelper", "Task Begin Date: " + task.getBeginDate());
            Log.d("DatabaseHelper", "Task End Date: " + task.getMaxEndDate());
            Log.d("DatabaseHelper", "Task State: " + context.getString(R.string.to_do));
            Log.d("DatabaseHelper", "Task Context: " + task.getContext());
            Log.d("DatabaseHelper", "Task Project: " + task.getProjectName());
            Log.d("DatabaseHelper", "Task URL: " + task.getUrl());
            Log.d("DatabaseHelper", "User ID: " + userId);

            values.put(COLUMN_TASK_NAME, task.getName());
            values.put(COLUMN_TASK_DESCRIPTION, task.getDescription());
            values.put(COLUMN_TASK_ESTIMATE_DURATION, task.getEstimateDuration());
            values.put(COLUMN_TASK_BEGIN_DATE, task.getBeginDate());
            values.put(COLUMN_TASK_END_DATE, task.getMaxEndDate());
            values.put(COLUMN_TASK_STATE, context.getString(R.string.to_do));
            values.put(COLUMN_TASK_CONTEXT, task.getContext());
            values.put(COLUMN_TASK_PROJECT, task.getProjectName());
            values.put(COLUMN_TASK_URL, task.getUrl());
            values.put(COLUMN_USER_ID, userId);

            // Thêm log trước khi insert
            Log.d("DatabaseHelper", "Inserting task into database");

            db.insert(TABLE_TASK, null, values);

            // Log khi thành công
            Log.d("DatabaseHelper", "Task inserted successfully");

        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding task", e);
        } finally {
            db.close();
            Log.d("DatabaseHelper", "Database closed");
        }
    }

    public int getUserIdByUsername(String username) {
        int userId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USER,
                    new String[]{COLUMN_USER_ID},
                    COLUMN_USER_USERNAME + " = ?",
                    new String[]{username},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting user ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return userId;
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

    public List<Task> getTasksByState(String state, int userId) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DatabaseHelper", "Fetching tasks with state: " + state + " for user ID: " + userId);

        Cursor cursor = db.query(TABLE_TASK,
                new String[]{
                        COLUMN_TASK_ID,
                        COLUMN_TASK_NAME,
                        COLUMN_TASK_DESCRIPTION,
                        COLUMN_TASK_ESTIMATE_DURATION,
                        COLUMN_TASK_BEGIN_DATE,
                        COLUMN_TASK_END_DATE,
                        COLUMN_TASK_STATE,
                        COLUMN_TASK_CONTEXT,
                        COLUMN_TASK_PROJECT,
                        COLUMN_TASK_URL
                },
                COLUMN_TASK_STATE + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{state, String.valueOf(userId)},
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    State taskState = new State();
                    taskState.changeState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_STATE)), context);
                    Task task = new Task(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_ESTIMATE_DURATION)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_BEGIN_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_END_DATE)),
                            taskState,
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_CONTEXT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PROJECT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_URL))
                    );
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            Log.d("DatabaseHelper", "Number of tasks with state '" + state + "' for user ID: " + userId + ": " + cursor.getCount());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public Task getTaskById(int id, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASK,
                null,
                COLUMN_TASK_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(id), String.valueOf(userId)},
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            State taskState = new State();
            taskState.changeState(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_STATE)), context);
            Task task = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_ESTIMATE_DURATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_BEGIN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_END_DATE)),
                    taskState,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_CONTEXT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PROJECT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_URL))
            );
            cursor.close();
            return task;
        }
        return null;
    }

    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, task.getName());
        values.put(COLUMN_TASK_DESCRIPTION, task.getDescription());
        values.put(COLUMN_TASK_STATE, task.getStateTask().getStatue(context));
        values.put(COLUMN_TASK_BEGIN_DATE, task.getBeginDate());
        values.put(COLUMN_TASK_END_DATE, task.getMaxEndDate());

        int rowsUpdated = db.update(TABLE_TASK, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close();
        return rowsUpdated > 0;
    }



    public void deleteTask(int taskId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, COLUMN_TASK_ID + " = ? AND " + COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
        db.close();
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

        return user;
    }

}
