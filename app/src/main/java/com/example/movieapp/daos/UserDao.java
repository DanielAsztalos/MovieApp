package com.example.movieapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.movieapp.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT uid FROM user")
    List<Integer> getUserIds();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
