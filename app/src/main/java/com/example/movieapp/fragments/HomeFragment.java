package com.example.movieapp.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MoviesAdapter;
import com.example.movieapp.tasks.SearchAsyncTask;
import com.example.movieapp.tasks.TopMoviesAsyncTask;
import com.example.movieapp.tasks.TopMoviesLoadMore;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private int page;
    private boolean search;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootview.findViewById(R.id.rv_movies);
        page = 1;

        TopMoviesAsyncTask task = new TopMoviesAsyncTask(getContext(), (RecyclerView) rootview.findViewById(R.id.rv_movies));
        task.execute(page);

        search = false;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(manager != null && manager.findLastVisibleItemPosition() == 19 && !search) {
                    page++;
                    TopMoviesLoadMore task = new TopMoviesLoadMore(getContext(), recyclerView);
                    task.execute(page);
                }
            }
        });

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
                    search = true;
                    SearchAsyncTask task = new SearchAsyncTask(getContext(), recyclerView);
                    task.execute(newText);
                }

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                search = false;
                TopMoviesAsyncTask task = new TopMoviesAsyncTask(getContext(), recyclerView);
                task.execute(1);
                return false;
            }
        });

        return rootview;
    }

}
