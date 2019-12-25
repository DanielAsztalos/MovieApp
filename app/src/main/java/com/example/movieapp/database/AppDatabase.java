package com.example.movieapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.movieapp.daos.FavoriteDao;
import com.example.movieapp.daos.MovieDao;
import com.example.movieapp.daos.UserDao;
import com.example.movieapp.models.FavoriteMovies;
import com.example.movieapp.models.Movie;
import com.example.movieapp.models.User;

@Database(entities = {User.class, Movie.class, FavoriteMovies.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract FavoriteDao favoriteDao();
}
