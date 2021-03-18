package com.example.filmstoday.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.listeners.OnSimpleMovieClickListener
import com.example.filmstoday.models.movie.SimpleMovie
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.getDuration
import com.squareup.picasso.Picasso

class SimpleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val poster: ImageView = itemView.findViewById(R.id.ivPoster)
    private val movieName: TextView = itemView.findViewById(R.id.tvMovieName)
    private val movieReleaseYear: TextView = itemView.findViewById(R.id.tvReleaseYear)
    private val movieReleaseCountry: TextView = itemView.findViewById(R.id.tvReleaseCountry)
    private val movieDuration: TextView = itemView.findViewById(R.id.tvDuration)
    private val movieRating: TextView = itemView.findViewById(R.id.tvRating)

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.simple_item, parent, false)
    )

    fun bind(movieFullModel: SimpleMovie, onSimpleMovieClickListener: OnSimpleMovieClickListener) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL_SMALL}${movieFullModel.posterPath}")
            .placeholder(R.drawable.ic_poster_placeholder)
            .into(poster)

        movieName.text = movieFullModel.movieTitle
        movieReleaseYear.text = movieFullModel.movieReleaseDate.take(4)
        movieReleaseCountry.text = movieFullModel.movieReleaseCountry
        movieDuration.text = getDuration(movieFullModel.movieRuntime)
        movieRating.text = movieFullModel.movieRating.toInt().toString()

        itemView.setOnClickListener {
            onSimpleMovieClickListener.onItemCLick(movie = movieFullModel)
        }

    }
}