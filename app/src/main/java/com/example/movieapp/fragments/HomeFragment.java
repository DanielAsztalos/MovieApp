package com.example.movieapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.movieapp.R;
import com.example.movieapp.tasks.SearchAsyncTask;
import com.example.movieapp.tasks.TopMoviesAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootview.findViewById(R.id.rv_movies);

        TopMoviesAsyncTask task = new TopMoviesAsyncTask(getContext(), (RecyclerView) rootview.findViewById(R.id.rv_movies));
        task.execute(1);

        SearchView searchView = (SearchView) rootview.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){
                    SearchAsyncTask task = new SearchAsyncTask(getContext(), recyclerView);
                    task.execute(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0){
                    SearchAsyncTask task = new SearchAsyncTask(getContext(), recyclerView);
                    task.execute(newText);
                }

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                TopMoviesAsyncTask task = new TopMoviesAsyncTask(getContext(), recyclerView);
                task.execute(1);
                return false;
            }
        });

        return rootview;
    }

}
