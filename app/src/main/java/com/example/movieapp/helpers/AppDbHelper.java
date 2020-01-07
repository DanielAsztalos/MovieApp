package com.example.movieapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.movieapp.contracts.CinemasContract;
import com.example.movieapp.contracts.FavoriteContract;
import com.example.movieapp.contracts.MovieContract;
import com.example.movieapp.contracts.UserContract;

/**
 * This class does the CRUD functionality with the database
 */

public class AppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "App.db";

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserContract.SQL_CREATE_TABLE_USER);
        db.execSQL(MovieContract.SQL_CREATE_TABLE_MOVIE);
        db.execSQL(FavoriteContract.SQL_CREATE_TABLE_FAVORITE);
        db.execSQL(CinemasContract.SQL_CREATE_TABLE_CINEMAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserContract.SQL_DELETE_ENTRIES);
        db.execSQL(MovieContract.SQL_DELETE_ENTRIES);
        db.execSQL(FavoriteContract.SQL_DELETE_ENTRIES);
        db.execSQL(CinemasContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
