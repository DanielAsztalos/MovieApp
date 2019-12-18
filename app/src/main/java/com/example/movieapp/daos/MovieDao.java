package com.example.movieapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieapp.models.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM MOVIE WHERE ID = :mid")
    Movie getMovieById(int mid);

    @Query("SELECT * FROM MOVIE")
    List<Movie> getAllMovies();

    @Insert
    void insertAll(Movie... movies);

    @Delete
    void delete(Movie movie);
}
