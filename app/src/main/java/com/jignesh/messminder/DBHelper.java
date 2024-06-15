package com.jignesh.messminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_USERS = "users";

    // Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ENROLLMENT = "enr_no";
    private static final String COLUMN_BLOCK = "block";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_STATUS = "status";
    // Create table SQL query
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_BLOCK + " TEXT,"
                    + COLUMN_ENROLLMENT + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_STATUS + " TEXT"
                    + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Function to insert a new user into the database
    public void insertUser(String username, String email, String enrollment, String block, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ENROLLMENT, enrollment);
        values.put(COLUMN_BLOCK, block);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_STATUS,"0");
        db.insert(TABLE_USERS, null, values);
        db.close();
    }


    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }


    public String[] getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] columns = {
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_EMAIL,
                COLUMN_ENROLLMENT,
                COLUMN_BLOCK,
                COLUMN_PASSWORD
        };

        // Define the selection criteria
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_USERS,        // Table to query
                columns,            // Columns to return
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // don't group the rows
                null,               // don't filter by row groups
                null                // don't sort the order
        );
        String[] userDetailsArray = null;
        // Check if the cursor contains any data
        if (cursor.moveToFirst()) {
            // Initialize the array with the appropriate size
            userDetailsArray = new String[cursor.getColumnCount()];

            // Retrieve user details from cursor and store them in the array
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                userDetailsArray[i] = cursor.getString(i);
            }
        }

        // Close cursor and database connection
        cursor.close();
        db.close();

        // Return the user object (null if not found)
        return userDetailsArray;
    }

    public List<String[]> getAllUsers() {
        List<String[]> usersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_EMAIL,
                COLUMN_ENROLLMENT,
                COLUMN_BLOCK,
                COLUMN_PASSWORD,
                COLUMN_STATUS
        };

        Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String[] user = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    user[i] = cursor.getString(i);
                }
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return usersList;
    }

}

