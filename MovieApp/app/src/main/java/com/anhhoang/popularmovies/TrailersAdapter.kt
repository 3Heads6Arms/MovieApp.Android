package com.anhhoang.popularmovies

/*
 * Copyright (C) 2013 The Android Open Source Project
 */

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.anhhoang.popularmovies.model.Trailer

import java.util.ArrayList

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.trailer_item_view.view.*

/**
 * Created by AnhHo on 3/22/2017.
 */

class TrailersAdapter(private val onTrailerClickListener: OnTrailerClickListener) : RecyclerView.Adapter<TrailersAdapter.ViewHolder>() {
    interface OnTrailerClickListener {
        fun onClick(trailerKey: String);
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val thumbnailIv = itemView.thumbnailIv
        val trailerTitleTv = itemView.trailerTitleTv

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val key: String = itemView.tag.toString()
            onTrailerClickListener.onClick(key)
        }
    }

    var trailerData: List<Trailer> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = trailerData.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = trailerData[position]
        val thumbnailUrl = "https://img.youtube.com/vi/${trailer.key}/1.jpg"
        holder.itemView.tag = trailer.key
        holder.trailerTitleTv.text = trailer.name

        Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .into(holder.thumbnailIv);
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.trailer_item_view, parent, false)

        return ViewHolder(view)
    }
}
