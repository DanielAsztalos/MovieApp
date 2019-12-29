package com.example.movieapp.tasks;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.movieapp.helpers.Constants;
import com.example.movieapp.models.Movie;
import com.example.movieapp.retrofit.MovieService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsAsyncTask extends AsyncTask<Integer, Void, Boolean> {
    private Context mContext;
    private Dialog mDialog;
    private int mId;

    public DetailsAsyncTask(Context context, Dialog dialog) {
        mContext = context;
        mDialog = dialog;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        mId = integers[0];

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

            Movie m = new Movie();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
