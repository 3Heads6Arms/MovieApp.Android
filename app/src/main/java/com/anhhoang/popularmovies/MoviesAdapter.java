/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anhhoang.popularmovies.data.Movie;
import com.anhhoang.popularmovies.utils.MoviesApiService;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> mMovieData;
    private Context mContext;

    public MoviesAdapter(Context context) {
        mContext = context;
    }

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
        Movie movie = mMovieData.get(position);
        String posterUrl = MoviesApiService.getMovieImageUrl(movie.getPosterPath());

        holder.mTitleTv.setText(movie.getTitle());
        holder.mRateTv.setText(String.valueOf(movie.getVoteAverage()));

        Glide.with(mContext)
                .load(posterUrl)
                .fitCenter()
                .into(holder.mPosterIv);
    }

    @Override
    public int getItemCount() {
        return mMovieData != null ? mMovieData.size() : 0;
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPosterIv;
        public TextView mTitleTv;
        public TextView mRateTv;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mPosterIv = (ImageView) itemView.findViewById(R.id.iv_poster);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
            mRateTv = (TextView) itemView.findViewById(R.id.tv_rate);
        }
    }
}
