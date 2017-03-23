package com.anhhoang.popularmovies.utils;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */


import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.text.TextUtils;

import com.anhhoang.popularmovies.data.IntegerRealm;
import com.anhhoang.popularmovies.data.MovieRealm;
import com.anhhoang.popularmovies.model.IMovie;
import com.anhhoang.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by AnhHo on 3/23/2017.
 */

public class FavoriteMovieUtils {
    public static final String[] COLUMNS = {
            "id",
            "title",
            "overview",
            "genres_id",
            "poster_path",
            "backdrop_path",
            "adult",
            "release_date",
            "popularity",
            "vote_count",
            "vote_average",
            "video",
            "user_favorite"
    };

    public static final int ID_COLUMN_INDEX = 0;
    public static final int TITLE_COLUMN_INDEX = 1;
    public static final int OVERVIEW_COLUMN_INDEX = 2;
    public static final int GENRES_COLUMN_INDEX = 3;
    public static final int POSTER_COLUMN_INDEX = 4;
    public static final int BACKDROP_COLUMN_INDEX = 5;
    public static final int ADULT_COLUMN_INDEX = 6;
    public static final int RELEASE_DATE_COLUMN_INDEX = 7;
    public static final int POPULARITY_COLUMN_INDEX = 8;
    public static final int VOTE_COUNT_COLUMN_INDEX = 9;
    public static final int VOTE_AVERAGE_COLUMN_INDEX = 10;
    public static final int VIDEO_COLUMN_INDEX = 11;
    public static final int USER_FAV_COLUMN_INDEX = 12;

    public static MatrixCursor parseAll(List<MovieRealm> movieRealms) {
        MatrixCursor matrixCursor = new MatrixCursor(COLUMNS);
        for (MovieRealm movieRealm : movieRealms) {
            matrixCursor.addRow(parse(movieRealm));
        }

        return matrixCursor;
    }

    public static Object[] parse(MovieRealm movieRealm) {
        String genres = TextUtils.join(";", movieRealm.getGenreIds());

        return new Object[]{
                movieRealm.getId(),
                movieRealm.getTitle(),
                movieRealm.getOverview(),
                genres,
                movieRealm.getPosterPath(),
                movieRealm.getBackdropPath(),
                movieRealm.isAdult() ? 1 : 0,
                movieRealm.getReleaseDate().getTime(),
                movieRealm.getPopularity(),
                movieRealm.getVoteCount(),
                movieRealm.getVoteAverage(),
                movieRealm.isHasVideo() ? 1 : 0,
                movieRealm.isUserFavorite() ? 1 : 0
        };
    }

    public static MovieRealm parse(ContentValues contentValues) {
        String genresStr = contentValues.getAsString(COLUMNS[GENRES_COLUMN_INDEX]);
        String[] genresArray = TextUtils.split(genresStr, ";");
        RealmList<IntegerRealm> genres = new RealmList<>();
        for (String genre : genresArray) {
            genres.add(new IntegerRealm(Integer.valueOf(genre)));
        }

        MovieRealm movieRealm = new MovieRealm();
        movieRealm.setId(contentValues.getAsInteger(COLUMNS[ID_COLUMN_INDEX]));
        movieRealm.setTitle(contentValues.getAsString(COLUMNS[TITLE_COLUMN_INDEX]));
        movieRealm.setOverview(contentValues.getAsString(COLUMNS[OVERVIEW_COLUMN_INDEX]));
        movieRealm.setGenreIds(genres);
        movieRealm.setPosterPath(contentValues.getAsString(COLUMNS[POSTER_COLUMN_INDEX]));
        movieRealm.setBackdropPath(contentValues.getAsString(COLUMNS[BACKDROP_COLUMN_INDEX]));
        movieRealm.setAdult(contentValues.getAsBoolean(COLUMNS[ADULT_COLUMN_INDEX]));
        movieRealm.setReleaseDate(new Date(contentValues.getAsLong(COLUMNS[RELEASE_DATE_COLUMN_INDEX])));
        movieRealm.setPopularity(contentValues.getAsDouble(COLUMNS[POPULARITY_COLUMN_INDEX]));
        movieRealm.setVoteCount(contentValues.getAsInteger(COLUMNS[VOTE_COUNT_COLUMN_INDEX]));
        movieRealm.setVoteAverage(contentValues.getAsDouble(COLUMNS[VOTE_AVERAGE_COLUMN_INDEX]));
        movieRealm.setHasVideo(contentValues.getAsBoolean(COLUMNS[VIDEO_COLUMN_INDEX]));
        movieRealm.setUserFavorite(contentValues.getAsBoolean(COLUMNS[USER_FAV_COLUMN_INDEX]));

        return movieRealm;
    }

    public static List<Movie> parse(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) return null;
        List<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()) {
            String genresStr = cursor.getString(GENRES_COLUMN_INDEX);
            String[] genresArray = TextUtils.split(genresStr, ";");
            List<Integer> genres = new ArrayList<>();
            for (String genre : genresArray) {
                genres.add(Integer.valueOf(genre));
            }

            Movie movie = new Movie();
            movie.setId(cursor.getInt(ID_COLUMN_INDEX));
            movie.setTitle(cursor.getString(TITLE_COLUMN_INDEX));
            movie.setOverview(cursor.getString(OVERVIEW_COLUMN_INDEX));
            movie.setGenreIds(genres);
            movie.setPosterPath(cursor.getString(POSTER_COLUMN_INDEX));
            movie.setBackdropPath(cursor.getString(BACKDROP_COLUMN_INDEX));
            movie.setAdult(cursor.getInt(ADULT_COLUMN_INDEX) != 0);
            movie.setReleaseDate(new Date(cursor.getLong(RELEASE_DATE_COLUMN_INDEX)));
            movie.setPopularity(cursor.getDouble(POPULARITY_COLUMN_INDEX));
            movie.setVoteCount(cursor.getInt(VOTE_COUNT_COLUMN_INDEX));
            movie.setVoteAverage(cursor.getDouble(VOTE_AVERAGE_COLUMN_INDEX));
            movie.setHasVideo(cursor.getInt(VIDEO_COLUMN_INDEX) != 0);
            movie.setUserFavorite(cursor.getInt(USER_FAV_COLUMN_INDEX) != 0);

            movies.add(movie);
        }

        return movies;
    }

    public static ContentValues parse(IMovie movie) {
        String genres = TextUtils.join(";", movie.getGenreIds());

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMNS[ID_COLUMN_INDEX], movie.getId());
        contentValues.put(COLUMNS[TITLE_COLUMN_INDEX], movie.getTitle());
        contentValues.put(COLUMNS[OVERVIEW_COLUMN_INDEX], movie.getOverview());
        contentValues.put(COLUMNS[GENRES_COLUMN_INDEX], genres);
        contentValues.put(COLUMNS[POSTER_COLUMN_INDEX], movie.getPosterPath());
        contentValues.put(COLUMNS[BACKDROP_COLUMN_INDEX], movie.getBackdropPath());
        contentValues.put(COLUMNS[ADULT_COLUMN_INDEX], movie.isAdult());
        contentValues.put(COLUMNS[RELEASE_DATE_COLUMN_INDEX], movie.getReleaseDate().getTime());
        contentValues.put(COLUMNS[POPULARITY_COLUMN_INDEX], movie.getPopularity());
        contentValues.put(COLUMNS[VOTE_COUNT_COLUMN_INDEX], movie.getVoteCount());
        contentValues.put(COLUMNS[VOTE_AVERAGE_COLUMN_INDEX], movie.getVoteAverage());
        contentValues.put(COLUMNS[VIDEO_COLUMN_INDEX], movie.isHasVideo());
        contentValues.put(COLUMNS[USER_FAV_COLUMN_INDEX], movie.isUserFavorite());

        return contentValues;
    }

    private FavoriteMovieUtils() {
    }
}
