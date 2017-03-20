package com.anhhoang.popularmovies;
/*
 * Copyright (C) 2013 The Android Open Source Project
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhhoang.popularmovies.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AnhHo on 3/21/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> mReviews;
    private int mTotalPages;
    private int mCurrentPage;
    private int mTotalReviews;

    public ReviewsAdapter(){
        mCurrentPage = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean attachToRoot = false;
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.review_item_view, parent, attachToRoot);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);

        holder.mAuthorNameTv.setText(review.getAuthor());
        holder.mReviewTv.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews != null ? mReviews.size() : 0;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(int totalPages) {
        this.mTotalPages = totalPages;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    public int getTotalReviews() {
        return mTotalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.mTotalReviews = totalReviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_author_name)
        public TextView mAuthorNameTv;
        @BindView(R.id.tv_review)
        public TextView mReviewTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
