package com.example.movieapp.retrofit;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import okhttp3.Response;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MovieService {
    @GET("movie/top_rated")
    Call<JsonElement> topRated(@QueryMap Map<String, String> options);

    @GET("search/movie")
    Call<JsonElement> searchResult(@QueryMap Map<String, String> options);

    @GET("movie/{id}")
    Call<JsonElement> getMovieDetails(@Path("id") int id, @QueryMap Map<String, String> options);

    @GET("movie/{id}/images")
    Call<JsonElement> getImages(@Path("id") int id, @QueryMap Map<String, String> options);

    @GET("movie/{id}/videos")
    Call<JsonElement> getVideos(@Path("id") int id, @QueryMap Map<String, String> options);

    @GET("movie/{id}/similar")
    Call<JsonElement> getSimilar(@Path("id") int id, @QueryMap Map<String, String> options);
}
