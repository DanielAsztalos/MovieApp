package com.example.movieapp.contracts;

import android.provider.BaseColumns;

/**
 * This class defines the structure of the user table in the database
 */

public final class UserContract {
    private UserContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_PROFILE = "path_to_pic";
    }

    public static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " ( " +
            UserEntry._ID + " INTEGER PRIMARY KEY, " +
            UserEntry.COLUMN_NAME_USERNAME + " TEXT, " +
            UserEntry.COLUMN_NAME_EMAIL + " TEXT, " +
            UserEntry.COLUMN_NAME_PASSWORD + " TEXT, " +
            UserEntry.COLUMN_NAME_PROFILE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
}
