package com.anhhoang.popularmovies.data;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by AnhHo on 3/22/2017.
 */


// Workaround for primitive types in realm (for list)
public class IntegerRealm extends RealmObject {
    public int value;

    public IntegerRealm() {
    }

    public IntegerRealm(int value) {
        this.value = value;
    }

    public static RealmList<IntegerRealm> parse(List<Integer> values) {
        RealmList<IntegerRealm> results = new RealmList<>();
        for (int value : values) {
            results.add(new IntegerRealm(value));
        }

        return results;
    }

    public static List<Integer> parseTo(RealmList<IntegerRealm> values) {
        List<Integer> results = new ArrayList<>();
        for (IntegerRealm integerRealm : values) {
            results.add(integerRealm.value);
        }

        return results;
    }
}
