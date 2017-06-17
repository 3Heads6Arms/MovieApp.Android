/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 1/29/2017.
 */
package com.anhhoang.popularmovies.utils

import com.anhhoang.popularmovies.BuildConfig
import com.anhhoang.popularmovies.model.GenreResponse
import com.anhhoang.popularmovies.model.MovieResponse
import com.anhhoang.popularmovies.model.ReviewResponse
import com.anhhoang.popularmovies.model.TrailerResponse

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


// Singleton to keep our call from one instance
class MoviesApiService private constructor() {

    /**
     * Retrofit Api Definition
     */
    private interface MovieApi {
        @GET("movie/popular")
        fun getMoviesByPopularity(@Query("api_key") apiKey: String): Call<MovieResponse>

        @GET("movie/top_rated")
        fun getMoviesByTopRating(@Query("api_key") apiKey: String): Call<MovieResponse>

        @GET("genre/movie/list")
        fun getGenres(@Query("api_key") apiKey: String): Call<GenreResponse>

        @GET("movie/{movie_id}/reviews")
        fun getReviews(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String, @Query("page") page: Int): Call<ReviewResponse>

        @GET("movie/{movie_id}/videos")
        fun getTrailers(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Call<TrailerResponse>
    }

    private val MOVIE_API_URL = "https://api.themoviedb.org/3/"

    private val mRetrofit: Retrofit
    private val mMovieApi: MovieApi

    init {
        mRetrofit = Retrofit.Builder()
                .baseUrl(MOVIE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        mMovieApi = mRetrofit.create(MovieApi::class.java)
    }

    /**
     * Asynchronously get list of movies by their popularity

     * @param callback - Callback that will be invoked when request is done.
     * *                 May contain result or error from the request.
     */
    fun getMoviesByPopularity() = mMovieApi.getMoviesByPopularity(BuildConfig.MOVIE_API_KEY)


    /**
     * Asynchronously get list of movies by their top rating

     * @param callback - Callback that will be invoked when request is done.
     * *                 May contain result or error from the request.
     */
    fun getMoviesByTopRating() = mMovieApi.getMoviesByTopRating(BuildConfig.MOVIE_API_KEY)


    /**
     * Get all movie's genres.

     * @param callback - Callback to be invoked when request is done.
     * *                 May contain result or error from the request.
     */
    fun getGenres() = mMovieApi.getGenres(BuildConfig.MOVIE_API_KEY)

    /**
     * Returns all reviews for a movie

     * @param movieId  - Id of the movie to get reviews for
     * *
     * @param page     - page of review, should be 1 first time
     * *
     * @param callback - Callback to be invoked when request is done.
     * *                 May contain result or error from request.
     */
    fun getReviews(movieId: Int, page: Int) = mMovieApi.getReviews(movieId, BuildConfig.MOVIE_API_KEY, page)

    /**
     * Returns all trailers for a movie

     * @param movieId  - Id of the movie to get trailers for
     * *
     * @param page     - page of review, should be 1 first time
     * *
     * @param callback - Callback to be invoked when request is done.
     * *                 May contain result or error from request.
     */
    fun getTrailers(movieId: Int) = mMovieApi.getTrailers(movieId, BuildConfig.MOVIE_API_KEY)

    companion object {
        private val MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/"
        const val YOUTUBE_URL = "http://www.youtube.com/watch?v="

        /**
         * Constructs full url to image server

         * @param path - Relative path to the image
         * *
         * @return full path to the image
         */
        fun getMovieImageUrl(path: String, size: MoviePosterSizeEnum): String = "$MOVIE_POSTER_URL$size$path"

        /**
         * Get instance of API helper class

         * @return instance of api helper class
         */
        val INSTANCE: MoviesApiService by lazy { MoviesApiService() }
    }
}
