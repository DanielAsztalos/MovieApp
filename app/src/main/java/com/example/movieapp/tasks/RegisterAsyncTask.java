package com.example.movieapp.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.movieapp.MainActivity;
import com.example.movieapp.R;
import com.example.movieapp.contracts.UserContract;
import com.example.movieapp.fragments.LoginFragment;
import com.example.movieapp.helpers.AppDbHelper;
import com.example.movieapp.models.User;

/**
 * This async task handles the register functionality
 * params:
 *      context: app context
 *
 * expects:
 *      user: data that should be saved to the db
 */

public class RegisterAsyncTask extends AsyncTask<User, Void, Boolean> {
    private boolean where = true;
    private Context mContext;


    public RegisterAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(User... users) {
        User user = users[0];
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // check if username was used
        String[] projection1 = {BaseColumns._ID};
        String selection1 = UserContract.UserEntry.COLUMN_NAME_USERNAME + " = ? ";
        String[] selectionArgs1 = {user.getUsername()};

        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, projection1, selection1, selectionArgs1,
                null, null, null);

        if(cursor.getCount() > 0) {
            where = true;
            cursor.close();
            return false;
        }

        // check if email was used
        String[] projection2 = {BaseColumns._ID};
        String selection2 = UserContract.UserEntry.COLUMN_NAME_EMAIL + " = ? ";
        String[] selectionArgs2 = {user.getEmail()};

        cursor = db.query(UserContract.UserEntry.TABLE_NAME, projection2, selection2, selectionArgs2,
                null, null, null);

        if(cursor.getCount() > 0) {
            where = false;
            cursor.close();
            return false;
        }

        cursor.close();

        db = dbHelper.getWritableDatabase();

        // save data to db
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_USERNAME, user.getUsername());
        values.put(UserContract.UserEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, user.getPassword());
        values.put(UserContract.UserEntry.COLUMN_NAME_PROFILE, user.getPathToProfilePic());

        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success) {
            Toast.makeText(mContext, R.string.registerSuccessful, Toast.LENGTH_LONG).show();
            if(mContext instanceof MainActivity) {
                // load the login fragment
                ((MainActivity) mContext).loadFragment(new LoginFragment());
            }
        }
        else
        {
            if(where) {
                Toast.makeText(mContext, R.string.usernameUsed, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(mContext, R.string.emailUsed, Toast.LENGTH_LONG).show();
            }
        }
    }
}
