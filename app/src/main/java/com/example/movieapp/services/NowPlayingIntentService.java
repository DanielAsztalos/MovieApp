package com.example.movieapp.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.movieapp.contracts.CinemasContract;
import com.example.movieapp.contracts.MovieContract;
import com.example.movieapp.helpers.AppDbHelper;
import com.example.movieapp.helpers.Constants;
import com.example.movieapp.models.Movie;
import com.example.movieapp.retrofit.MovieService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This service downloads movies that were released no more than 3 days ago
 * or will be released in no more than 3 days
 */

public class NowPlayingIntentService extends IntentService {

    public NowPlayingIntentService() {
        super("NowPlayingIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // get db instance
        AppDbHelper dbHelper = new AppDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete the old InCinemas movies
        db.delete(CinemasContract.CinemasEntry.TABLE_NAME, null, null);

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_API_KEY, Constants.api_key);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // get movies
        MovieService service = retrofit.create(MovieService.class);

        int maxPage = 10;
        int page = 1;
        ArrayList<Movie> movie = new ArrayList<>();
        // parse list of movies
        while (page < maxPage) {
            params.put(Constants.PARAM_PAGE, String.valueOf(page));

            JsonElement response = null;

            try{
                response = service.getNowPlaying(params).execute().body();
            }
            catch (Exception e) {
                Log.d("SERVICE", e.getMessage());
            }

            boolean toAdd = false;

            if(response != null) {
                JsonArray movies = new JsonArray();
                try {
                    movies = response.getAsJsonObject().getAsJsonArray("results");
                    maxPage = response.getAsJsonObject().get("total_pages").getAsInt();
                } catch (Exception e) {
                    Log.d("SERVICE", "doInBackground: " + e.getMessage());
                }

                for (int i = 0; i < movies.size(); i++) {
                    Movie m = new Movie();
                    try {
                        m.setId(movies.get(i).getAsJsonObject().get("id").getAsInt());
                    } catch (Exception e) {

                    }

                    try {
                        m.setTitle(movies.get(i).getAsJsonObject().get("title").getAsString());
                    } catch (Exception e) {
                    }

                    try {
                        m.setDescription(movies.get(i).getAsJsonObject().get("overview").getAsString());
                    } catch (Exception e) {
                    }

                    try {
                        m.setPathToPhoto(movies.get(i).getAsJsonObject().get("poster_path").getAsString());
                        String dt = movies.get(i).getAsJsonObject().get("release_date").getAsString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(dt);
                        Date d1, d2;
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, -3);
                        d1 = c.getTime();
                        c.add(Calendar.DATE, 6);
                        d2 = c.getTime();

                        // decide if the movies are eligible to be saved in the database
                        if(date.compareTo(d1) > 0 && date.compareTo(d2) < 0) {
                            toAdd = true;
                        }

                    } catch (Exception e) {
                    }

                    if(toAdd) {
                        // if yes save them
                        ContentValues cv = new ContentValues();

                        cv.put(MovieContract.MovieEntry._ID, String.valueOf(m.getId()));
                        cv.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, m.getTitle());
                        cv.put(MovieContract.MovieEntry.COLUMN_NAME_DESCRIPTION, m.getDescription());
                        cv.put(MovieContract.MovieEntry.COLUMN_NAME_PHOTO, m.getPathToPhoto());

                        long num = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, cv,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        ContentValues c2 = new ContentValues();
                        c2.put(CinemasContract.CinemasEntry.COLUMN_NAME_MOVIE_ID, String.valueOf(m.getId()));

                        num = db.insertWithOnConflict(CinemasContract.CinemasEntry.TABLE_NAME, null, c2,
                                SQLiteDatabase.CONFLICT_IGNORE);
                    }

                    toAdd = false;
                }

            }

            page++;
        }
    }
}
