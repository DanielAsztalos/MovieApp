package com.example.movieapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieapp.models.FavoriteMovies;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT movie_id FROM FAVORITEMOVIES WHERE user_id = :uid")
    List<Integer> getFavouriteMovieList(int uid);

    @Insert
    void addFavorite(FavoriteMovies favoriteMovies);

    @Delete
    void delete(FavoriteMovies favoriteMovies);
}
