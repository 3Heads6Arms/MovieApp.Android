package com.anhhoang.popularmovies.utils;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.net.Uri;

/**
 * Created by AnhHo on 3/23/2017.
 */

public class FavoriteMovieUriUtils {
    private FavoriteMovieUriUtils() {
    }

    public static final String AUTHORITY = "com.anhhoang.popularmovies";
    public static final String PATH_FAV_MOVIES = "favoriteMovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();
}
