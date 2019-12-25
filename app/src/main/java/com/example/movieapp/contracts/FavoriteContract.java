package com.example.movieapp.contracts;

import android.provider.BaseColumns;

public final class FavoriteContract {
    private FavoriteContract() {
    }

    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_UID = "user_id";
        public static final String COLUMN_NAME_MID = "movie_id";
    }

    public static final String SQL_CREATE_TABLE_FAVORITE =
            "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " ( " +
                    FavoriteEntry.COLUMN_NAME_UID + " INTEGER, " +
                    FavoriteEntry.COLUMN_NAME_MID + " INTEGER, PRIMARY KEY ( " +
                    FavoriteEntry.COLUMN_NAME_UID + ", " + FavoriteEntry.COLUMN_NAME_MID + " ) )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME;

}