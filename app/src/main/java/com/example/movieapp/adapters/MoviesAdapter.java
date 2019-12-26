package com.example.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.Movie;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private ArrayList<Movie> mMovies;
    private Context mContext;


    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        mMovies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_movie_row, parent, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
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
}


class MovieViewHolder extends RecyclerView.ViewHolder {
    TextView tv_title, tv_desc;
    ImageView iv_movie;

    public MovieViewHolder(View view){
        super(view);

        tv_title = view.findViewById(R.id.tv_movie_title);
        tv_desc = view.findViewById(R.id.tv_movie_description);
        iv_movie = view.findViewById(R.id.img_movie);
    }
}
