/*
 * Copyright (C) 2013 The Android Open Source Project
 */
/**
 * Created by AnhHo on 2/4/2017.
 */
package com.anhhoang.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
