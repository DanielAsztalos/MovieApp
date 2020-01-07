package com.example.movieapp.tasks;

import android.content.Context;
import android.net.Uri;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This async task gets the results to a search query from TMDB
 * params:
 *      context: app context
 *      recyclerView: the recyclerview from the homepage
 *
 * expects:
 *      keyword: search query
 */

public class SearchAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private Context mContext;
    private String mWord;
    private RecyclerView mRecyclerView;

    public SearchAsyncTask(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... keyword) {
        mWord = keyword[0];
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PARAM_API_KEY, Constants.api_key);
        try{
            params.put(Constants.PARAM_QUERY, URLEncoder.encode(mWord, StandardCharsets.UTF_8.toString()).replaceAll("%2B", "%20"));
        }catch (Exception e) {}

        ArrayList<Movie> moviesOnPage = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService service = retrofit.create(MovieService.class);

        JsonElement response = null;
        // get results to the search query
        try{
            response = service.searchResult(params).execute().body();

            //Log.d("TOP_MOVIES", "doInBackground: " + service.searchResult(params).execute().toString());
        }
        catch(Exception e){
            Log.d("TOP_MOVIES", "doInBackground: " + e.getMessage());
        }

        // parse results
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
                    m.setPathToPhoto(movies.get(i).getAsJsonObject().get("backdrop_path").getAsString());
                }catch (Exception e){}

                moviesOnPage.add(m);
            }

            return moviesOnPage;

        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        // if there are results put them in the recyclerview
        if (movies != null ){
            RecyclerView.LayoutManager movieLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(movieLayoutManager);
            MoviesAdapter adapter = new MoviesAdapter(mContext, movies);
            mRecyclerView.setAdapter(adapter);
        }

    }
}
