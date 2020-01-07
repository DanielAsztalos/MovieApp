package com.example.movieapp.tasks;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.contracts.UserContract;
import com.example.movieapp.helpers.AppDbHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This async task handles the saving of the path to the new profile picture to the db
 * params:
 *      context - the context of the app
 *
 * expects:
 *      string: new path to profile picture
 */

public class ChangeProfileImageAsyncTask extends AsyncTask<String, Void, Boolean> {
    private Context mContext;
    private String mPath;

    public ChangeProfileImageAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        mPath = strings[0];
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_NAME_PROFILE, mPath);

        String selection = UserContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {sharedPreferences.getString("id", "")};

        // update db
        int count = db.update(
                UserContract.UserEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        if(count > 0) {
            sharedPreferences.edit().putString("profile_path", mPath).commit();
            return true;
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        // if successful
        if (aBoolean) {
            // change image on profile page
            ((CircleImageView) ((Activity) mContext).findViewById(R.id.profile_image)).setImageURI(Uri.parse(mPath));
            Toast.makeText(mContext, R.string.changePicSuccess, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mContext, R.string.changePicFail, Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(aBoolean);
    }
}
