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

    public void discoverMovies(Callback<RequestResult<Movie>> callback) {
        mMovieApi.discoverMovies("5d70e3447b10f44c50bdf7e55e436fea")
                .enqueue(callback);
    }

    // TODO: Make size varies
    public static String getMovieImageUrl(String path) {
        String url = String.format("%s%s%s", MOVIE_POSTER_URL, MoviePosterSizeEnum.w185, path);
        return url;
    }

    public static MoviesApiService getService() {
        if (moviesApiService == null) {
            moviesApiService = new MoviesApiService();
        }

        return moviesApiService;
    }
}
