package com.example.movieapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

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

    private ArrayList<Integer> imageIds;

    private ArrayList<Integer> videoIds;

    private ArrayList<Movie> relatedMovies;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public void setPathToPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<Integer> getImageIds() {
        return imageIds;
    }

    public void setImageIds(ArrayList<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    public ArrayList<Integer> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(ArrayList<Integer> videoIds) {
        this.videoIds = videoIds;
    }

    public ArrayList<Movie> getRelatedMovies() {
        return relatedMovies;
    }

    public void setRelatedMovies(ArrayList<Movie> relatedMovies) {
        this.relatedMovies = relatedMovies;
    }
}
