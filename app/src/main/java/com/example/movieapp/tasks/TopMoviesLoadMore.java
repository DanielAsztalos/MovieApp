package com.example.movieapp.tasks;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.adapters.MoviesAdapter;
import com.example.movieapp.models.Movie;

import java.util.ArrayList;

/**
 * This async if triggered loads another page of the top rated movies
 * params:
 *      context: app context
 *      recyclerView: the rv that will contain the movies
 */

public class TopMoviesLoadMore extends TopMoviesAsyncTask {
     public TopMoviesLoadMore(Context context, RecyclerView recyclerView) {
         super(context, recyclerView);
     }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if(movies != null) {
            MoviesAdapter adapter = (MoviesAdapter) mRecyclerView.getAdapter();
            adapter.addMovies(movies);
        }
    }
}
