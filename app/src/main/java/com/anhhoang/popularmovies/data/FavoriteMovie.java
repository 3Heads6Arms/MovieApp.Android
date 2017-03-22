package com.anhhoang.popularmovies.data;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by AnhHo on 3/22/2017.
 */

public class FavoriteMovie extends RealmObject {
    @PrimaryKey
    private int id;
    @Required
    private int movieId;
}
