package com.example.movieapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteMovies {
    @PrimaryKey
    private int user_id;

    @PrimaryKey
    private int movie_id;
}
