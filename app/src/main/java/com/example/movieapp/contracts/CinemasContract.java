package com.example.movieapp.contracts;

import android.provider.BaseColumns;

public final class CinemasContract {

    private CinemasContract() {

    }

    public static class CinemasEntry implements BaseColumns {
        public static final String TABLE_NAME = "cinemas";
        public static final String COLUMN_NAME_MOVIE_ID = "mid";
    }

    public static final String SQL_CREATE_TABLE_CINEMAS = "CREATE TABLE " +
            CinemasEntry.TABLE_NAME + " ( " + CinemasEntry.COLUMN_NAME_MOVIE_ID +
            " INTEGER PRIMARY KEY)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CinemasEntry.TABLE_NAME;
}
