package com.anhhoang.popularmovies

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anhhoang.popularmovies.model.Genre

import kotlinx.android.synthetic.main.genre_item_view.view.*

/**
 * Created by AnhHo on 3/20/2017.
 */

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {
    var genres: List<Genre> = ArrayList()
        set(genres) {
            field = genres
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val attachImmediatelyToRoot = false
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.genre_item_view, parent, attachImmediatelyToRoot)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genres[position]
        holder.genreTv.text = genre.name
    }

    override fun getItemCount() = genres.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var genreTv = itemView.genreTv
    }
}
