package com.anhhoang.popularmovies.utils

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.LinearSnapHelper

/**
 * Created by AnhHo on 3/21/2017.
 */

class CustomLinearSnapHelper : LinearSnapHelper() {

    interface OnLinearSnapFling {
        fun onFling()
    }

    private var mOnSnapFling: OnLinearSnapFling? = null

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        mOnSnapFling?.onFling()

        return super.onFling(velocityX, velocityY)
    }

    fun setOnSnapFling(mOnSnapFling: OnLinearSnapFling) {
        this.mOnSnapFling = mOnSnapFling
    }
}
