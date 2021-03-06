package com.example.movieapp.tasks;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.adapters.ImagesAdapter;
import com.example.movieapp.adapters.RelatedAdapter;
import com.example.movieapp.contracts.FavoriteContract;
import com.example.movieapp.contracts.MovieContract;
import com.example.movieapp.helpers.AppDbHelper;
import com.example.movieapp.helpers.Constants;
import com.example.movieapp.models.Movie;
import com.example.movieapp.retrofit.MovieService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This async task loads the details about a movie from TMDB and displays them
 * params:
 *      context - the app context
 *      dialog - reference to the details dialog
 *
 * expects:
 *      integers: id of the selected movie
 */

public class DetailsAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    private Context mContext;
    private Dialog mDialog;
    private int mId;
    private Movie mMovie;
    private ArrayList<Movie> related;
    boolean inFavs = false;

    public DetailsAsyncTask(Context context, Dialog dialog) {
        mContext = context;
        mDialog = dialog;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        mId = integers[0];

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("LOGGED_USER", Context.MODE_PRIVATE);

        AppDbHelper dbHelper = new AppDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // check if the movie is in favorite movies
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

        if(cursor.getCount() > 0) {
            inFavs = true;
        }

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_API_KEY, Constants.api_key);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService service = retrofit.create(MovieService.class);

        JsonElement response = null;
        // get data from TMDB
        try{
            response = service.getMovieDetails(mId, params).execute().body();
        }catch (Exception e) {
            Log.d("DETAILS", "doInBackground: " + e.getMessage());
        }

        // parse data
        if(response != null) {
            JsonObject movie = new JsonObject();
            try {
                movie = response.getAsJsonObject();
            }catch (Exception e) {}

            mMovie = new Movie();

            try{
                mMovie.setId(movie.get("id").getAsInt());
            }catch (Exception e){}

            mMovie.setTitle(movie.get("title").getAsString());
            mMovie.setDescription(movie.get("overview").getAsString());
            mMovie.setPathToPhoto(movie.get("poster_path").getAsString());

            mMovie.setImagePaths(new ArrayList<>());
            mMovie.setRelatedMovies(new ArrayList<>());
            mMovie.setVideoIds(new ArrayList<>());
        }

        // get image paths for the movie
        try{
            response = service.getImages(mId, params).execute().body();
        }catch (Exception e) {
            Log.d("DETAILS", "doInBackground: " + e.getMessage());
        }
        // parse them
        if(response != null) {
            JsonArray backdrops = new JsonArray();
            try{
                backdrops = response.getAsJsonObject().get("backdrops").getAsJsonArray();
            } catch (Exception e) {}

            for(int i = 0; i < backdrops.size(); i++) {
                mMovie.getImagePaths().add(backdrops.get(i).getAsJsonObject().get("file_path").getAsString());
            }
        }
        // get video paths of the movie
        try{
            response = service.getVideos(mId, params).execute().body();
        }catch (Exception e) {
            Log.d("DETAILS", "doInBackground: " + e.getMessage());
        }
        // save
        if(response != null) {
            JsonArray results = new JsonArray();
            try{
                results = response.getAsJsonObject().get("results").getAsJsonArray();
                mMovie.getVideoIds().add(results.get(0).getAsJsonObject().get("key").getAsString());
            } catch (Exception e) {}
        }
        // get related movies
        try{
            response = service.getSimilar(mId, params).execute().body();
        }catch (Exception e) {
            Log.d("DETAILS", "doInBackground: " + e.getMessage());
        }
        // parse the results
        if(response != null) {
            JsonArray movies = new JsonArray();
            try{
                movies = response.getAsJsonObject().getAsJsonArray("results");
            }
            catch(Exception e) {
                Log.d("TOP_MOVIES", "doInBackground: " + e.getMessage());
            }

            related = new ArrayList<>();
            for(int i = 0; i < movies.size(); i++) {
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
                } catch (Exception e) {
                }

                related.add(m);
            }
        }


        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        // if successful
        if(aBoolean) {
            if(inFavs) {
                // if it's in the favorites db change icon of like button
                ((FloatingActionButton) mDialog.findViewById(R.id.fab)).setImageResource(R.drawable.ic_star_black_24dp);
            }

            // display movie title
            ((TextView) mDialog.findViewById(R.id.detail_title)).setText(mMovie.getTitle());

            // load poster photo
            if(mMovie.getPathToPhoto() != null && mMovie.getPathToPhoto().length() > 0) {
                Glide.with(mContext)
                        .load("https://image.tmdb.org/t/p/w500/" + mMovie.getPathToPhoto())
                        .into(((ImageView) mDialog.findViewById(R.id.img_poster_movie)));
            }

            // display description text
            ((TextView) mDialog.findViewById(R.id.tv_description_detail)).setText(mMovie.getDescription());

            // add images to the horizontal recyclerview
            RecyclerView rv_images = mDialog.findViewById(R.id.rv_images);
            LinearLayoutManager manager_images = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            rv_images.setLayoutManager(manager_images);
            ImagesAdapter adapter_images = new ImagesAdapter(mContext, mMovie);
            rv_images.setAdapter(adapter_images);

            // add video to layout if there's at least one video
            WebView video = ((WebView) mDialog.findViewById(R.id.video_details));
            if(mMovie.getVideoIds().size() > 0){
                String playvideo = "<html> <body> <iframe width=\"350\" height=\"250\" src=\"https://www.youtube.com/embed/" + mMovie.getVideoIds().get(0)
                        + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
                video.getSettings().setJavaScriptEnabled(true);
                video.loadData(playvideo, "text/html", "utf-8");

            }

            // add related movies to the layout
            if(related.size() > 0) {
                RecyclerView rv = mDialog.findViewById(R.id.rv_related);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                rv.setLayoutManager(manager);
                RelatedAdapter adapter = new RelatedAdapter(mContext, related);
                rv.setAdapter(adapter);
            }
        }
        super.onPostExecute(aBoolean);
    }
}
