package com.example.movieapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "user_name")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "path_to_profile_pic")
    private String pathToProfilePic;
}
