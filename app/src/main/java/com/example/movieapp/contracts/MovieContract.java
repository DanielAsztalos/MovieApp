package com.example.movieapp.contracts;

import android.provider.BaseColumns;

/**
 * This class defines the structure of the movie table in the database
 */

public final class MovieContract {
    private MovieContract(){}

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PHOTO = "path_to_photo";
        public static final String COLUMN_NAME_RELEASE = "release_date";
    }

    public static final String SQL_CREATE_TABLE_MOVIE =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                    MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    MovieEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    MovieEntry.COLUMN_NAME_PHOTO + " TEXT, " +
                    MovieEntry.COLUMN_NAME_RELEASE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
}
