/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.utils;

import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.data.RequestResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


// Singleton to keep our call from one instance
public final class MoviesApiService {
    private interface MovieApi {
        @GET("discover/movie")
        Call<RequestResult<Movie>> discoverMovies(@Query("api_key") String apiKey);
    }

    private final String MOVIE_API_URL = "https://api.themoviedb.org/3/";
    public String apiKey = "";
    private static final String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static MoviesApiService moviesApiService;

    private Retrofit mRetrofit;
    private MovieApi mMovieApi;

    private MoviesApiService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMovieApi = mRetrofit.create(MovieApi.class);
    }

    /**
     * Performs asynchronous request to server using background thread
     * @param callback - Callback functions that will be invoked when request is done.
     *                 Can contain result or error from the request.
     */
    public void discoverMovies(Callback<RequestResult<Movie>> callback) {
        mMovieApi.discoverMovies(apiKey)
                .enqueue(callback);
    }

    /**
     * Constructs full url to image server
     * @param path - Relative path to the image
     * @return full path to the image
     */
    public static String getMovieImageUrl(String path) {
        // TODO: Make size varies
        String url = String.format("%s%s%s", MOVIE_POSTER_URL, MoviePosterSizeEnum.w185, path);
        return url;
    }

    /**
     * Get instance of API helper class
     * @return instance of api helper class
     */
    public static MoviesApiService getService() {
        if (moviesApiService == null) {
            moviesApiService = new MoviesApiService();
        }

        return moviesApiService;
    }
}