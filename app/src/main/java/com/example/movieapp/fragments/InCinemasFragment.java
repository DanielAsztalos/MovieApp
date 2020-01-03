package com.example.movieapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;
import com.example.movieapp.tasks.InCinemasAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class InCinemasFragment extends Fragment {


    public InCinemasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_in_cinemas, container, false);

        RecyclerView recyclerView = rootview.findViewById(R.id.rv_cinemas);
        InCinemasAsyncTask task = new InCinemasAsyncTask(getContext(), recyclerView);
        task.execute();

        return rootview;
    }

}
