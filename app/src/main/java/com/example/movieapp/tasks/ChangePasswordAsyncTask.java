package com.example.movieapp.tasks;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.contracts.UserContract;
import com.example.movieapp.helpers.AppDbHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This async task handles the saving of the new password to the db
 * params:
 *      context: the app context
 *
 * expects:
 *      passes - the new and the old password
 */

public class ChangePasswordAsyncTask extends AsyncTask<String, Void, Boolean> {

    private Context mContext;
    private String mPass;

    public ChangePasswordAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        mPass = strings[0];
        String oldPass = strings[1];
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, mPass);

        String selection = UserContract.UserEntry._ID + " = ? AND " + UserContract.UserEntry.COLUMN_NAME_PASSWORD + " = ? ";
        String[] selectionArgs = {sharedPreferences.getString("id", ""), oldPass};

        // update password in the db
        int count = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        if(count > 0) {
            return true;
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            Toast.makeText(mContext, R.string.changePassSuccess, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mContext, R.string.changePassFail, Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(aBoolean);
    }
}
