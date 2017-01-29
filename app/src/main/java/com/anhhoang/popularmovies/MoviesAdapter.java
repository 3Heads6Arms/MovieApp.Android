/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private String[] mMovieData;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attachImmediatelyToRoot = false;
        View movieView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_item_view, parent, attachImmediatelyToRoot);
        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
//        holder.mMovieTitleTv.setText(mMovieData[position]);
    }

    @Override
    public int getItemCount() {
        return mMovieData != null ? mMovieData.length : 0;
    }

    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView mMovieTitleTv;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mMovieTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
