package com.anhhoang.popularmovies.data;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AnhHo on 3/23/2017.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 6;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_QUERY = "CREATE TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_GENRES_ID + " TEXT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ADULT + " NUMERIC, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " NUMERIC, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POPULARITY + " REAL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VIDEO + " NUMERIC, " +
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_FAVORITE + " NUMERIC);";

        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
