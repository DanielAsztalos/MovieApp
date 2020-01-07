package com.example.movieapp.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.movieapp.MainSectionActivity;
import com.example.movieapp.R;
import com.example.movieapp.contracts.UserContract;
import com.example.movieapp.helpers.AppDbHelper;

/**
 * This async task handles the login functionality
 * params:
 *      - context: app context
 *      - username: the username that the user typed in
 *      - password
 */

public class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;
    private String mUserName;
    private String mPassword;

    public LoginAsyncTask(Context context, String username, String password) {
        mContext = context;
        mUserName = username;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {UserContract.UserEntry._ID, UserContract.UserEntry.COLUMN_NAME_USERNAME,
                UserContract.UserEntry.COLUMN_NAME_EMAIL, UserContract.UserEntry.COLUMN_NAME_PROFILE};
        String selections = UserContract.UserEntry.COLUMN_NAME_USERNAME + " = ? AND " +
                UserContract.UserEntry.COLUMN_NAME_PASSWORD + " = ? ";
        String[] selectionArgs = {mUserName, mPassword};

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selections,
                selectionArgs,
                null,
                null,
                null
        );

        // if there's no user with the given username + pass combination
        if(cursor.getCount() < 1) {
            // login failed
            return false;
        }

        cursor.moveToFirst();

        // save user data to shared preferences
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.UserEntry._ID))));
        editor.putString("username", cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_USERNAME)));
        editor.putString("email", cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_EMAIL)));
        editor.putString("profile_path", cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME_PROFILE)));
        editor.commit();

        dbHelper.close();

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            Toast.makeText(mContext, R.string.loginSuccess, Toast.LENGTH_LONG).show();

            // navigate to the home screen
            Intent intent = new Intent(mContext, MainSectionActivity.class);
            mContext.startActivity(intent);
        }
        else{
            Toast.makeText(mContext, R.string.loginFail, Toast.LENGTH_LONG).show();
        }
    }
}
