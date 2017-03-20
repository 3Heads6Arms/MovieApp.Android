package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

/**
 * Created by AnhHo on 3/20/2017.
 */

public class Trailer {
    private String id;
    private String iso6391;
    private String iso31161;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getId() {
        return id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public String getIso31161() {
        return iso31161;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
