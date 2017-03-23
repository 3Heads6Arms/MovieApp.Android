package com.anhhoang.popularmovies.data;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by AnhHo on 3/23/2017.
 */

public class FavoriteMovieContract {
    public static final String AUTHORITY = "com.anhhoang.popularmovies";
    public static final String PATH_FAV_MOVIES = "favoriteMovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final String TABLE_NAME = "FavoriteMovies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_GENRES_ID = "genres_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_USER_FAVORITE = "user_favorite";
    }
}
