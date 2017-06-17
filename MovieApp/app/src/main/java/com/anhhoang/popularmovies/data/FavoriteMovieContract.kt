package com.anhhoang.popularmovies.data

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by AnhHo on 3/23/2017.
 */

object FavoriteMovieContract {
    val AUTHORITY = "com.anhhoang.popularmovies"
    val PATH_FAV_MOVIES = "favoriteMovies"

    val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY)

    class FavoriteMovieEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build()
            const val TABLE_NAME = "FavoriteMovies"
            const val COLUMN_TITLE = "title"
            const val COLUMN_OVERVIEW = "overview"
            const val COLUMN_GENRES_ID = "genres_id"
            const val COLUMN_POSTER_PATH = "poster_path"
            const val COLUMN_BACKDROP_PATH = "backdrop_path"
            const val COLUMN_ADULT = "adult"
            const val COLUMN_RELEASE_DATE = "release_date"
            const val COLUMN_POPULARITY = "popularity"
            const val COLUMN_VOTE_COUNT = "vote_count"
            const val COLUMN_VOTE_AVERAGE = "vote_average"
            const val COLUMN_VIDEO = "video"
            const val COLUMN_USER_FAVORITE = "user_favorite"
        }
    }
}
