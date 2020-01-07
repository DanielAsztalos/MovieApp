package com.example.movieapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.MainSectionActivity;
import com.example.movieapp.R;
import com.example.movieapp.dialogs.DetailDialog;
import com.example.movieapp.models.Movie;

import java.util.ArrayList;

/**
 * This adapter is responsible for displaying the cards of the movies
 * in Home, Favorite and InCinemas fragments
 * params:
 * *context - the context of the application
 * *movies - the movies to display
 */

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private ArrayList<Movie> mMovies;
    private Context mContext;


    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        mMovies = movies;
        mContext = context;
    }

    public void addMovies(ArrayList<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_movie_row, parent, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails(position);
            }
        });
        holder.tv_title.setText(mMovies.get(position).getTitle());
        String desc = mMovies.get(position).getDescription();
        if(desc.length() > 151) {
            desc = desc.substring(0, 150) + "...";
        }
        holder.tv_desc.setText(desc);
        if(mMovies.get(position).getPathToPhoto() != null && mMovies.get(position).getPathToPhoto().length() > 0) {
            Glide.with(mContext)
                    .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(position).getPathToPhoto())
            .into(holder.iv_movie);
        }

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    // This function opens the details dialog for the selected movie
    private void openDetails(int position){
        DetailDialog dialog = new DetailDialog();
        FragmentTransaction ft = ((MainSectionActivity) mContext).getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("id", mMovies.get(position).getId());
        dialog.setArguments(bundle);
        dialog.show(ft, "DETAIL_DIALOG");
    }
}


class MovieViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title, tv_desc;
    ImageView iv_movie;
    RelativeLayout layout;

    public MovieViewHolder(View view){
        super(view);

        tv_title = view.findViewById(R.id.tv_movie_title);
        tv_desc = view.findViewById(R.id.tv_movie_description);
        iv_movie = view.findViewById(R.id.img_movie);
        layout = view.findViewById(R.id.layout_card);
    }
}
