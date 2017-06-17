package com.anhhoang.popularmovies.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.provider.BaseColumns._ID

import com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.Companion.TABLE_NAME

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

/**
 * Created by AnhHo on 3/23/2017.
 */

class FavoriteMovieContentProvider : ContentProvider() {
    private lateinit var mFavoriteMovieDbHelper: FavoriteMovieDbHelper

    override fun onCreate(): Boolean {
        // Not creating global Realm Db access, because we want to close realm instance each time in case of memory leaking
        mFavoriteMovieDbHelper = FavoriteMovieDbHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // Due to limitation of Realm.io we won't be able to use Projection, selection and etc.
        val match = sUriMatcher.match(uri)
        val db = mFavoriteMovieDbHelper.readableDatabase
        val cursor: Cursor
        when (match) {
            FAVORITE_MOVIES -> cursor = db.query(
                    TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            FAVORITE_MOVIES_WITH_ID -> {
                val id = java.lang.Long.parseLong(uri.lastPathSegment)
                cursor = db.query(
                        TABLE_NAME,
                        projection,
                        _ID + "=?",
                        arrayOf(id.toString()), null, null, null
                )
            }
            else -> throw UnsupportedOperationException("Not supported uri: " + uri)
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        val uriResult: Uri?
        val database = mFavoriteMovieDbHelper.writableDatabase
        when (match) {
            FAVORITE_MOVIES -> {
                val id = database.insert(TABLE_NAME, null, values)
                if (id > 0) {
                    uriResult = ContentUris.withAppendedId(uri, id)
                } else {
                    throw SQLException("Failed to insert row into " + uri)
                }
            }
            else -> throw UnsupportedOperationException("Not supported uri: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)

        return uriResult
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val match = sUriMatcher.match(uri)
        val result: Int
        val db = mFavoriteMovieDbHelper.writableDatabase
        when (match) {
            FAVORITE_MOVIES_WITH_ID -> {
                val id = uri.lastPathSegment

                result = db.delete(TABLE_NAME, _ID + "=?", arrayOf(id))
            }
            else -> throw UnsupportedOperationException("Not supported uri: " + uri)
        }
        if (result != 0) {
            context.contentResolver.notifyChange(uri, null)
        }

        return result
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not supported operation")
    }

    companion object {
        val FAVORITE_MOVIES = 100
        val FAVORITE_MOVIES_WITH_ID = 101

        private val sUriMatcher = buildUriMatcher()

        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAV_MOVIES, FAVORITE_MOVIES)
            uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAV_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID)

            return uriMatcher
        }
    }
}
