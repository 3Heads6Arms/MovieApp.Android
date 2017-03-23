package com.anhhoang.popularmovies;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.app.Application;

import io.realm.Realm;

/**
 * Created by AnhHo on 3/22/2017.
 */

public class PopularMoviesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
