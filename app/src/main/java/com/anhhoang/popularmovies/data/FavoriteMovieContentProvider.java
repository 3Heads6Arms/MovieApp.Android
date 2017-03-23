package com.anhhoang.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.anhhoang.popularmovies.utils.FavoriteMovieUtils;
import com.anhhoang.popularmovies.utils.FavoriteMovieUriUtils;

import io.realm.Realm;
import io.realm.RealmResults;

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

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavoriteMovieUriUtils.AUTHORITY, FavoriteMovieUriUtils.PATH_FAV_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMovieUriUtils.AUTHORITY, FavoriteMovieUriUtils.PATH_FAV_MOVIES + "/#", FAVORITE_MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // Not creating global Realm Db access, because we want to close realm instance each time in case of memory leaking
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Due to limitation of Realm.io we won't be able to use Projection, selection and etc.
        int match = sUriMatcher.match(uri);
        MatrixCursor resultCursor = null;
        Realm realm = Realm.getDefaultInstance();
        try {
            switch (match) {
                case FAVORITE_MOVIES:
                    realm.beginTransaction();
                    RealmResults<MovieRealm> results = realm.where(MovieRealm.class).findAll();
                    if (results.size() > 0) {
                        resultCursor = FavoriteMovieUtils.parseAll(results);
                    }
                    realm.commitTransaction();
                    break;
                case FAVORITE_MOVIES_WITH_ID:
                    realm.beginTransaction();
                    int id = Integer.parseInt(uri.getLastPathSegment());
                    RealmResults<MovieRealm> queryResults = realm.where(MovieRealm.class).equalTo("id", id).findAll();
                    if (queryResults.size() > 0) {
                        resultCursor = FavoriteMovieUtils.parseAll(queryResults);
                    }
                    realm.commitTransaction();
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported uri: " + uri);
            }
        } finally {
            realm.close();
        }
        return resultCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = sUriMatcher.match(uri);
        Uri uriResult = null;
        Realm realm = Realm.getDefaultInstance();
        try {
            switch (match) {
                case FAVORITE_MOVIES:
                    final MovieRealm movieRealm = FavoriteMovieUtils.parse(values);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<MovieRealm> results = realm.where(MovieRealm.class).equalTo("id", movieRealm.getId()).findAll();
                            if (results.size() <= 0) {
                                realm.copyToRealm(movieRealm);
                            }
                        }
                    });

                    uriResult = uri.buildUpon()
                            .appendPath(String.valueOf(movieRealm.getId()))
                            .build();
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported uri: " + uri);
            }
        } finally {
            realm.close();
        }
        return uriResult;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int result = 0;
        Realm realm = Realm.getDefaultInstance();
        try {
            switch (match) {
                case FAVORITE_MOVIES_WITH_ID:
                    int id = Integer.parseInt(uri.getLastPathSegment());
                    realm.beginTransaction();
                    RealmResults<MovieRealm> queryResults = realm.where(MovieRealm.class).equalTo("id", id).findAll();
                    result = queryResults.size() > 0 ? 1 : 0;
                    queryResults.deleteAllFromRealm();
                    realm.commitTransaction();
                    break;
                default:
                    throw new UnsupportedOperationException("Not supported uri: " + uri);
            }
        } finally {
            realm.close();
        }
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not supported operation");
    }
}
