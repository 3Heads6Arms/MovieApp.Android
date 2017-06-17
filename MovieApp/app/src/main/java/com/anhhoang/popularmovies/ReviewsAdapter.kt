package com.anhhoang.popularmovies

/*
 * Copyright (C) 2013 The Android Open Source Project
 */


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anhhoang.popularmovies.model.Review
import kotlinx.android.synthetic.main.review_item_view.view.*


/**
 * Created by AnhHo on 3/21/2017.
 */

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    var reviews: List<Review> = ArrayList()
        set(reviews) {
            field = reviews
            notifyDataSetChanged()
        }
    var totalPages: Int = 0
    var currentPage: Int = 0
    var totalReviews: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val attachToRoot = false
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.review_item_view, parent, attachToRoot)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]

        holder.mAuthorNameTv.text = review.author
        holder.mReviewTv.text = review.content
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mAuthorNameTv = itemView.tv_author_name
        var mReviewTv = itemView.tv_review

    }
}
