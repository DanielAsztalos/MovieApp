package com.example.movieapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;
import com.example.movieapp.tasks.FavoritesAsyncTask;

/**
 * This fragment displays the favorite movies of a user
 */
public class FavoritesFragment extends Fragment {
    private View rootview;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_favorites, container, false);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        // start the task that adds the movies to the list
        FavoritesAsyncTask task = new FavoritesAsyncTask(getContext(), rootview.findViewById(R.id.rv_favorites));
        task.execute();
    }
}
