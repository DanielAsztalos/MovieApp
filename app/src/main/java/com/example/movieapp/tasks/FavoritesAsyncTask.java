package com.example.movieapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MoviesAdapter;
import com.example.movieapp.contracts.FavoriteContract;
import com.example.movieapp.contracts.MovieContract;
import com.example.movieapp.helpers.AppDbHelper;
import com.example.movieapp.models.Movie;

import java.util.ArrayList;

public class FavoritesAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private RecyclerView mRecyclerView;

    public FavoritesAsyncTask(Context context, RecyclerView rv){
        mContext = context;
        mRecyclerView = rv;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        ArrayList<Integer> ids = new ArrayList<>();
        mMovies = new ArrayList<>();

        String[] projection = {FavoriteContract.FavoriteEntry.COLUMN_NAME_MID};
        String selection = FavoriteContract.FavoriteEntry.COLUMN_NAME_UID + " = ? ";
        String[] selectionArgs = {sharedPreferences.getString("id", "")};

        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
                );

        while(cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteEntry.COLUMN_NAME_MID)));
        }

        String[] projection1 = {MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_NAME_TITLE,
                MovieContract.MovieEntry.COLUMN_NAME_DESCRIPTION,
                MovieContract.MovieEntry.COLUMN_NAME_PHOTO
        };
        selection = MovieContract.MovieEntry._ID + " = ?";

        for(int id: ids) {
            Movie m = new Movie();
            String[] selectionArgs1 = {String.valueOf(id)};
            cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                    projection1,
                    selection,
                    selectionArgs1,
                    null,
                    null,
                    null);

            cursor.moveToFirst();

            m.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID)));
            m.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE)));
            m.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_DESCRIPTION)));
            m.setPathToPhoto(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_PHOTO)));

            mMovies.add(m);
        }

        dbHelper.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aBoolean) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        MoviesAdapter adapter = new MoviesAdapter(mContext, mMovies);
        mRecyclerView.setAdapter(adapter);
        super.onPostExecute(aBoolean);
    }
}
