package com.example.movieapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Movie {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "path_to_photo")
    private String pathToPhoto;

    @ColumnInfo(name = "release_date")
    private long releaseDate;
}
