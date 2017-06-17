/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.anhhoang.popularmovies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anhhoang.popularmovies.model.Movie
import com.anhhoang.popularmovies.utils.MoviePosterSizeEnum
import com.anhhoang.popularmovies.utils.MoviesApiService
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item_view.view.*

import java.util.ArrayList


class MoviesAdapter(private val mOnClickListener: MoviesAdapter.OnMovieItemClickListener) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    interface OnMovieItemClickListener {
        fun onClick(movie: Movie)
    }

    var movieData: MutableList<Movie> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val attachImmediatelyToRoot = false
        val movieView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.movie_item_view, parent, attachImmediatelyToRoot)
        return MovieViewHolder(movieView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieData[position]

        holder.mTitleTv.text = movie.title
        holder.mRateTv.text = movie.voteAverage.toString()
        holder.itemView.tag = movie

        if (movie.posterPath != null) {
            val posterUrl = MoviesApiService.getMovieImageUrl(movie.posterPath!!, MoviePosterSizeEnum.w185)
            Glide.with(holder.itemView.context)
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(holder.mPosterIv!!)
        }
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mPosterIv = itemView.iv_poster
        var mTitleTv = itemView.tv_title
        var mRateTv = itemView.tv_rate

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val movie = v.tag as Movie

            mOnClickListener.onClick(movie)
        }
    }
}
