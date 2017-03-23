package com.anhhoang.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.anhhoang.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

/**
 * Created by AnhHo on 3/23/2017.
 */

public class FavoriteMovieContentProvider extends ContentProvider {
    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAV_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAV_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // Not creating global Realm Db access, because we want to close realm instance each time in case of memory leaking
        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Due to limitation of Realm.io we won't be able to use Projection, selection and etc.
        int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (match) {
            case FAVORITE_MOVIES:
                cursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_MOVIES_WITH_ID:
                long id = Long.parseLong(uri.getLastPathSegment());
                cursor = db.query(
                        TABLE_NAME,
                        projection,
                        FavoriteMovieContract.FavoriteMovieEntry._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Not supported uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        Uri uriResult = null;
        final SQLiteDatabase database = mFavoriteMovieDbHelper.getWritableDatabase();
        switch (match) {
            case FAVORITE_MOVIES:
                long id = database.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    uriResult = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not supported uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return uriResult;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int result = 0;
        SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();
        switch (match) {
            case FAVORITE_MOVIES_WITH_ID:
                String id = uri.getLastPathSegment();

                result = db.delete(TABLE_NAME, FavoriteMovieContract.FavoriteMovieEntry._ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Not supported uri: " + uri);
        }
        if (result != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported operation");
    }
}
