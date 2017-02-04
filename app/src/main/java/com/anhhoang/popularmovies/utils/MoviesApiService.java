/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.utils;

import com.anhhoang.popularmovies.BuildConfig;
import com.anhhoang.popularmovies.data.Genre;
import com.anhhoang.popularmovies.data.GenreResponse;
import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.data.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


// Singleton to keep our call from one instance
public final class MoviesApiService {

    /**
     * Retrofit Api Definition
     */
    private interface MovieApi {
        @GET("discover/movie")
        Call<MovieResponse<Movie>> discoverMovies(@Query("api_key") String apiKey);

        @GET("movie/popular")
        Call<MovieResponse<Movie>> getMoviesByPopularity(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Call<MovieResponse<Movie>> getMoviesByTopRating(@Query("api_key") String apiKey);

        @GET("genre/movie/list")
        Call<GenreResponse> getGenres(@Query("api_key") String apiKey);
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

    /**
     * Asynchronously get list of movies
     *
     * @param callback - Callback that will be invoked when request is done.
     *                 Can contain result or error from the request.
     */
    public void discoverMovies(Callback<MovieResponse<Movie>> callback) {
        mMovieApi.discoverMovies(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Asynchronously get list of movies by their popularity
     *
     * @param callback - Callback that will be invoked when request is done.
     *                 Can contain result or error from the request.
     */
    public void getMoviesByPopularity(Callback<MovieResponse<Movie>> callback) {
        mMovieApi.getMoviesByPopularity(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Asynchronously get list of movies by their top rating
     *
     * @param callback - Callback that will be invoked when request is done.
     *                 Can contain result or error from the request.
     */
    public void getMoviesByTopRating(Callback<MovieResponse<Movie>> callback) {
        mMovieApi.getMoviesByTopRating(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Get all movie's genres.
     * @param callback - Callback to be invoked when request is done.
     *                 Can contain result or error from the request.
     */
    public void getGenres(Callback<GenreResponse> callback) {
        mMovieApi.getGenres(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Constructs full url to image server
     *
     * @param path - Relative path to the image
     * @return full path to the image
     */
    public static String getMovieImageUrl(String path, MoviePosterSizeEnum size) {
        String url = String.format("%s%s%s", MOVIE_POSTER_URL, size, path);
        return url;
    }

    /**
     * Get instance of API helper class
     *
     * @return instance of api helper class
     */
    public static MoviesApiService getService() {
        if (moviesApiService == null) {
            moviesApiService = new MoviesApiService();
        }

        return moviesApiService;
    }
}
