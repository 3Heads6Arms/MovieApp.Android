package com.anhhoang.popularmovies.model;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AnhHo on 3/20/2017.
 */

public class Trailer implements Parcelable {
    private String id;
    private String iso6391;
    private String iso31161;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Trailer(Parcel source) {
        id = source.readString();
        iso6391 = source.readString();
        iso31161 = source.readString();
        key = source.readString();
        name = source.readString();
        site = source.readString();
        size = source.readInt();
        type = source.readString();
    }

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

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso6391);
        dest.writeString(iso31161);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
