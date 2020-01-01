package com.example.movieapp.tasks;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.nsd.NsdManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.contracts.FavoriteContract;
import com.example.movieapp.contracts.MovieContract;
import com.example.movieapp.helpers.AppDbHelper;
import com.example.movieapp.helpers.Constants;
import com.example.movieapp.models.Movie;
import com.example.movieapp.retrofit.MovieService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddFavoriteAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    private Context mContext;
    private Dialog mDialog;

    public AddFavoriteAsyncTask(Context context, Dialog dialog){
        mContext = context;
        mDialog = dialog;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        int mId = integers[0];
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        String[] projection = {FavoriteContract.FavoriteEntry.COLUMN_NAME_UID};
        String selection = FavoriteContract.FavoriteEntry.COLUMN_NAME_UID + " = ? AND " +
                FavoriteContract.FavoriteEntry.COLUMN_NAME_MID + " = ? ";
        String[] selectionArgs = {sharedPreferences.getString("id", ""), String.valueOf(mId)};

        Cursor cursor = db.query(
                FavoriteContract.FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor.getCount() == 0) {
            db = dbHelper.getWritableDatabase();

            Movie selected = new Movie();

            Map<String, String> params = new HashMap<>();
            params.put(Constants.PARAM_API_KEY, Constants.api_key);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieService service = retrofit.create(MovieService.class);

            JsonElement response = null;

            try{
                response = service.getMovieDetails(mId, params).execute().body();
            }catch (Exception e) {
                Log.d("DETAILS", "doInBackground: " + e.getMessage());
            }

            if(response != null) {
                JsonObject movie = new JsonObject();
                try {
                    movie = response.getAsJsonObject();
                }catch (Exception e) {}

                try{
                    selected.setId(movie.get("id").getAsInt());
                }catch (Exception e){}

                selected.setTitle(movie.get("title").getAsString());
                selected.setDescription(movie.get("overview").getAsString());
                selected.setPathToPhoto(movie.get("poster_path").getAsString());
            }

            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry._ID, selected.getId());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, selected.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_DESCRIPTION, selected.getDescription());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_PHOTO, selected.getPathToPhoto());

            long num = db.insert(
                    MovieContract.MovieEntry.TABLE_NAME,
                    null,
                    values
            );

            values = new ContentValues();
            values.put(FavoriteContract.FavoriteEntry.COLUMN_NAME_UID, sharedPreferences.getString("id", ""));
            values.put(FavoriteContract.FavoriteEntry.COLUMN_NAME_MID, selected.getId());

            num = db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);

            return true;
        }
        else{
            selection = FavoriteContract.FavoriteEntry.COLUMN_NAME_UID + " = ? AND " +
                    FavoriteContract.FavoriteEntry.COLUMN_NAME_MID + " = ?";
            String[] selectionArgs1 = {sharedPreferences.getString("id", ""), String.valueOf(mId)};

            db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs1);
            return false;
        }



    }

    @Override
    protected void onPostExecute(Boolean bool) {
        FloatingActionButton fab = mDialog.findViewById(R.id.fab);
        if(bool) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
            Toast.makeText(mContext, R.string.favSaved, Toast.LENGTH_LONG).show();
        }
        else{
            fab.setImageResource(R.drawable.ic_star_border_black_24dp);
            Toast.makeText(mContext, R.string.favRemoved, Toast.LENGTH_LONG).show();
        }

        super.onPostExecute(bool);
    }
}
