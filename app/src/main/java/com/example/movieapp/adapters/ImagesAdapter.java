package com.example.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.models.Movie;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {
    private Context mContext;
    private Movie mMovie;

    public ImagesAdapter(Context context, Movie movie) {
        mContext = context;
        mMovie = movie;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.movie_image_item, parent, false);
        return new ImagesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w500/" + mMovie.getImagePaths().get(position))
                .fitCenter()
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return mMovie.getImagePaths().size();
    }
}

class ImagesViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_image;

    public ImagesViewHolder(View view) {
        super(view);

        iv_image = view.findViewById(R.id.iv_movie_image);
    }
}
