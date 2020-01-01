package com.example.movieapp.adapters;

import android.content.Context;
import android.os.Bundle;
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

public class RelatedAdapter extends RecyclerView.Adapter<RelatedViewHolder> {
    private Context mContext;
    private ArrayList<Movie> mMovies;

    public RelatedAdapter(Context context, ArrayList<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.related_movie_item, parent, false);
        return new RelatedViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails(position);
            }
        });
        holder.tv_title.setText(mMovies.get(position).getTitle());
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(position).getPathToPhoto())
                .centerCrop()
                .into(holder.iv_poster);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    private void openDetails(int position){
        DetailDialog dialog = new DetailDialog();
        FragmentTransaction ft = ((MainSectionActivity) mContext).getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("id", mMovies.get(position).getId());
        dialog.setArguments(bundle);
        dialog.show(ft, "DETAIL_DIALOG");
    }
}

class RelatedViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_poster;
    TextView tv_title;
    RelativeLayout layout;

    public RelatedViewHolder(View view) {
        super(view);

        iv_poster = view.findViewById(R.id.item_image);
        tv_title = view.findViewById(R.id.item_title);
        layout = view.findViewById(R.id.rl_related);
    }
}
