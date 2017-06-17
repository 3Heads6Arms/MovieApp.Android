package com.anhhoang.popularmovies.data

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_ADULT
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_BACKDROP_PATH
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_GENRES_ID
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_OVERVIEW
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_POPULARITY
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_POSTER_PATH
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_RELEASE_DATE
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_TITLE
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_USER_FAVORITE
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_VIDEO
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_VOTE_AVERAGE
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.COLUMN_VOTE_COUNT
import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.TABLE_NAME

/**
 * Created by AnhHo on 3/23/2017.
 */

class FavoriteMovieDbHelper(context: Context) : SQLiteOpenHelper(context, FavoriteMovieDbHelper.DATABASE_NAME, null, FavoriteMovieDbHelper.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_QUERY = """CREATE TABLE $TABLE_NAME (
                $_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_OVERVIEW TEXT,
                $COLUMN_GENRES_ID TEXT,
                $COLUMN_POSTER_PATH TEXT,
                $COLUMN_BACKDROP_PATH TEXT,
                $COLUMN_ADULT NUMERIC,
                $COLUMN_RELEASE_DATE NUMERIC,
                $COLUMN_POPULARITY REAL,
                $COLUMN_VOTE_COUNT INTEGER,
                $COLUMN_VOTE_AVERAGE REAL,
                $COLUMN_VIDEO NUMERIC,
                $COLUMN_USER_FAVORITE NUMERIC);"""

        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private val DATABASE_NAME = "moviesDb.db"
        private val VERSION = 1
    }
}
