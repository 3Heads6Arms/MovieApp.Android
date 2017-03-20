package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by AnhHo on 3/20/2017.
 */

public class Review {
    @SerializedName("id")
    private String id;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;
}
