package com.example.movieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.adapters.MoviesAdapter;
import com.example.movieapp.helpers.Constants;
import com.example.movieapp.models.Movie;
import com.example.movieapp.retrofit.MovieService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopMoviesAsyncTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {
    protected Context mContext;
    protected int mPage;
    protected RecyclerView mRecyclerView;

    public TopMoviesAsyncTask(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Integer... page) {
        mPage = page[0];

        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_API_KEY, Constants.api_key);
        params.put(Constants.PARAM_PAGE, String.valueOf(mPage));

        ArrayList<Movie> moviesOnPage = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService service = retrofit.create(MovieService.class);

        JsonElement response = null;
        try{
            response = service.topRated(params).execute().body();

            //Log.d("TOP_MOVIES", "doInBackground: " + service.topRated(params).execute().body().toString());
        }
        catch(Exception e){
            Log.d("TOP_MOVIES", "doInBackground: " + e.getMessage());
        }

        if(response != null) {
            JsonArray movies = new JsonArray();
            try{
                movies = response.getAsJsonObject().getAsJsonArray("results");
            }
            catch(Exception e) {
                Log.d("TOP_MOVIES", "doInBackground: " + e.getMessage());
            }

            for(int i = 0; i < movies.size(); i++) {
                Movie m = new Movie();
                try {
                    m.setId(movies.get(i).getAsJsonObject().get("id").getAsInt());
                }catch (Exception e) {

                }

                try{
                    m.setTitle(movies.get(i).getAsJsonObject().get("title").getAsString());
                }
                catch (Exception e){}

                try{
                    m.setDescription(movies.get(i).getAsJsonObject().get("overview").getAsString());
                }catch (Exception e){}

                try{
                    m.setPathToPhoto(movies.get(i).getAsJsonObject().get("poster_path").getAsString());
                }catch (Exception e){}

                moviesOnPage.add(m);
            }

            return moviesOnPage;

        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies != null ){
            RecyclerView.LayoutManager movieLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(movieLayoutManager);
            MoviesAdapter adapter = new MoviesAdapter(mContext, movies);
            mRecyclerView.setAdapter(adapter);
        }

    }
}
