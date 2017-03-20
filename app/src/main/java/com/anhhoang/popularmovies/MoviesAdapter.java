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

import com.anhhoang.popularmovies.model.Movie;
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum;
import com.anhhoang.popularmovies.utils.MoviesApiService;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    public interface OnMovieItemClickListener {
        void onClick(Movie movie);
    }

    private List<Movie> mMovieData;
    private Context mContext;
    private OnMovieItemClickListener mOnClickListener;

    public MoviesAdapter(Context context, OnMovieItemClickListener clickListener) {
        mContext = context;
        mOnClickListener = clickListener;
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

        holder.mTitleTv.setText(movie.getTitle());
        holder.mRateTv.setText(String.valueOf(movie.getVoteAverage()));

        if (movie.getPosterPath() != null) {
            String posterUrl = MoviesApiService.getMovieImageUrl(movie.getPosterPath(), MoviePosterSizeEnum.w185);
            Glide.with(mContext)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.mPosterIv);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieData != null ? mMovieData.size() : 0;
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_poster)
        public ImageView mPosterIv;
        @BindView(R.id.tv_title)
        public TextView mTitleTv;
        @BindView(R.id.tv_rate)
        public TextView mRateTv;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = mMovieData.get(position);

            mOnClickListener.onClick(movie);
        }
    }
}
