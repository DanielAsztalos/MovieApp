package com.example.movieapp.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.movieapp.R;
import com.example.movieapp.tasks.AddFavoriteAsyncTask;
import com.example.movieapp.tasks.DetailsAsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This dialog displays the details about a selected movie
 */

public class DetailDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make it a fullscreen dialog
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_detail, container, false);

        // get the id of the selected movie than get it from TMDB
        int id = getArguments().getInt("id");
        DetailsAsyncTask task = new DetailsAsyncTask(getContext(), getDialog());
        task.execute(id);

        // set toolbar close icon
        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        // save movie to favorites on floating like button click
        FloatingActionButton btn = view.findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFavoriteAsyncTask task = new AddFavoriteAsyncTask(getContext(), getDialog());
                task.execute(id);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        // make the dialog expand to the full size of the window
        if(dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

