/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.utils;

import com.anhhoang.popularmovies.BuildConfig;
import com.anhhoang.popularmovies.model.GenreResponse;
import com.anhhoang.popularmovies.model.MovieResponse;
import com.anhhoang.popularmovies.model.ReviewResponse;
import com.anhhoang.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


// Singleton to keep our call from one instance
public final class MoviesApiService {

    /**
     * Retrofit Api Definition
     */
    private interface MovieApi {
        @GET("movie/popular")
        Call<MovieResponse> getMoviesByPopularity(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Call<MovieResponse> getMoviesByTopRating(@Query("api_key") String apiKey);

        @GET("genre/movie/list")
        Call<GenreResponse> getGenres(@Query("api_key") String apiKey);

        @GET("movie/{movie_id}/reviews")
        Call<ReviewResponse> getReviews(@Query("api_key") String apiKey, @Path("movie_id") int movieId, @Query("page") int page);

        @GET("movie/{movie_id}/videos")
        Call<TrailerResponse> getTrailers(@Query("api_key") String apiKey, @Path("movie_id") int movieId, @Query("page") int page);
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
     * Asynchronously get list of movies by their popularity
     *
     * @param callback - Callback that will be invoked when request is done.
     *                 May contain result or error from the request.
     */
    public void getMoviesByPopularity(Callback<MovieResponse> callback) {
        mMovieApi.getMoviesByPopularity(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Asynchronously get list of movies by their top rating
     *
     * @param callback - Callback that will be invoked when request is done.
     *                 May contain result or error from the request.
     */
    public void getMoviesByTopRating(Callback<MovieResponse> callback) {
        mMovieApi.getMoviesByTopRating(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     * Get all movie's genres.
     * @param callback - Callback to be invoked when request is done.
     *                 May contain result or error from the request.
     */
    public void getGenres(Callback<GenreResponse> callback) {
        mMovieApi.getGenres(BuildConfig.MOVIE_API_KEY)
                .enqueue(callback);
    }

    /**
     *  Returns all reviews for a movie
     * @param movieId - Id of the movie to get reviews for
     * @param page - page of review, should be 1 first time
     * @param callback - Callback to be invoked when request is done.
     *                 May contain result or error from request.
     */
    public void getReviews(int movieId, int page, Callback<ReviewResponse> callback){
        mMovieApi.getReviews(BuildConfig.MOVIE_API_KEY, movieId, page)
                .enqueue(callback);
    }

    /**
     *  Returns all trailers for a movie
     * @param movieId - Id of the movie to get trailers for
     * @param page - page of review, should be 1 first time
     * @param callback - Callback to be invoked when request is done.
     *                 May contain result or error from request.
     */
    public void getTrailers(int movieId, int page, Callback<TrailerResponse> callback){
        mMovieApi.getTrailers(BuildConfig.MOVIE_API_KEY, movieId, page)
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
