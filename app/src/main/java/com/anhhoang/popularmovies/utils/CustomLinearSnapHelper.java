package com.anhhoang.popularmovies.utils;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.LinearSnapHelper;

/**
 * Created by AnhHo on 3/21/2017.
 */

public class CustomLinearSnapHelper extends LinearSnapHelper {

    public interface OnLinearSnapFling {
        void onFling();
    }

    private OnLinearSnapFling mOnSnapFling;

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        if(mOnSnapFling != null){
            mOnSnapFling.onFling();
        }

        return super.onFling(velocityX, velocityY);
    }

    public void setOnSnapFling(OnLinearSnapFling mOnSnapFling) {
        this.mOnSnapFling = mOnSnapFling;
    }
}
