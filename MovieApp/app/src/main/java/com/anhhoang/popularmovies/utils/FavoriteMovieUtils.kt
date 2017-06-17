package com.anhhoang.popularmovies.utils

/*
 * Copyright (C) 2013 The Android Open Source Project
 */


import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns._ID
import android.text.TextUtils

import com.anhhoang.popularmovies.data.FavoriteMovieContract
import com.anhhoang.popularmovies.model.Movie

import java.util.ArrayList
import java.util.Date

/**
 * Created by AnhHo on 3/23/2017.
 */

object FavoriteMovieUtils {
    val COLUMNS = arrayOf(
            _ID,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_GENRES_ID,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ADULT,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POPULARITY,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VIDEO,
            FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_FAVORITE)

    const val ID_COLUMN_INDEX = 0
    const val TITLE_COLUMN_INDEX = 1
    const val OVERVIEW_COLUMN_INDEX = 2
    const val GENRES_COLUMN_INDEX = 3
    const val POSTER_COLUMN_INDEX = 4
    const val BACKDROP_COLUMN_INDEX = 5
    const val ADULT_COLUMN_INDEX = 6
    const val RELEASE_DATE_COLUMN_INDEX = 7
    const val POPULARITY_COLUMN_INDEX = 8
    const val VOTE_COUNT_COLUMN_INDEX = 9
    const val VOTE_AVERAGE_COLUMN_INDEX = 10
    const val VIDEO_COLUMN_INDEX = 11
    const val USER_FAV_COLUMN_INDEX = 12

    fun parse(cursor: Cursor?): List<Movie>? {
        val movies = ArrayList<Movie>()
        cursor?.let {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val genresStr = it.getString(GENRES_COLUMN_INDEX)
                    val genresArray = TextUtils.split(genresStr, ";")
                    val genres = ArrayList<Int>()
                    for (genre in genresArray) {
                        genres.add(Integer.valueOf(genre))
                    }

                    val movie = Movie()
                    movie.id = it.getInt(ID_COLUMN_INDEX)
                    movie.title = it.getString(TITLE_COLUMN_INDEX)
                    movie.overview = it.getString(OVERVIEW_COLUMN_INDEX)
                    movie.genreIds = genres
                    movie.posterPath = it.getString(POSTER_COLUMN_INDEX)
                    movie.backdropPath = it.getString(BACKDROP_COLUMN_INDEX)
                    movie.isAdult = it.getInt(ADULT_COLUMN_INDEX) != 0
                    movie.releaseDate = Date(it.getLong(RELEASE_DATE_COLUMN_INDEX))
                    movie.popularity = it.getDouble(POPULARITY_COLUMN_INDEX)
                    movie.voteCount = it.getInt(VOTE_COUNT_COLUMN_INDEX)
                    movie.voteAverage = it.getDouble(VOTE_AVERAGE_COLUMN_INDEX)
                    movie.isHasVideo = it.getInt(VIDEO_COLUMN_INDEX) != 0
                    movie.isUserFavorite = it.getInt(USER_FAV_COLUMN_INDEX) != 0

                    movies.add(movie)
                }
            }
        }

        return movies
    }

    fun parse(movie: Movie): ContentValues {
        val genres = TextUtils.join(";", movie.genreIds)

        val contentValues = ContentValues()
        contentValues.put(COLUMNS[ID_COLUMN_INDEX], movie.id)
        contentValues.put(COLUMNS[TITLE_COLUMN_INDEX], movie.title)
        contentValues.put(COLUMNS[OVERVIEW_COLUMN_INDEX], movie.overview)
        contentValues.put(COLUMNS[GENRES_COLUMN_INDEX], genres)
        contentValues.put(COLUMNS[POSTER_COLUMN_INDEX], movie.posterPath)
        contentValues.put(COLUMNS[BACKDROP_COLUMN_INDEX], movie.backdropPath)
        contentValues.put(COLUMNS[ADULT_COLUMN_INDEX], movie.isAdult)
        contentValues.put(COLUMNS[RELEASE_DATE_COLUMN_INDEX], movie.releaseDate!!.time)
        contentValues.put(COLUMNS[POPULARITY_COLUMN_INDEX], movie.popularity)
        contentValues.put(COLUMNS[VOTE_COUNT_COLUMN_INDEX], movie.voteCount)
        contentValues.put(COLUMNS[VOTE_AVERAGE_COLUMN_INDEX], movie.voteAverage)
        contentValues.put(COLUMNS[VIDEO_COLUMN_INDEX], movie.isHasVideo)
        contentValues.put(COLUMNS[USER_FAV_COLUMN_INDEX], movie.isUserFavorite)

        return contentValues
    }
}
