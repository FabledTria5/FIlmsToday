package com.example.filmstoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.models.movie.Movie
import com.example.filmstoday.utils.Constants.Companion.POSTERS_BASE_URL_SMALL
import com.squareup.picasso.Picasso

class SearchMovieAdapter : RecyclerView.Adapter<SearchMovieAdapter.SearchMoviesViewHolder>() {

    private val moviesList = arrayListOf<Movie>()

    inner class SearchMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.ivSearchMoviePoster)

        fun bindMovie(movie: Movie) {
            if (movie.poster_path != null) {
                Picasso.get().load("$POSTERS_BASE_URL_SMALL${movie.poster_path}")
                    .placeholder(R.drawable.ic_poster_placeholder)
                    .into(poster)
            } else {
                poster.setImageResource(R.drawable.ic_poster_placeholder)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchMoviesViewHolder(
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movies_search_item, parent, false)
    )

    override fun onBindViewHolder(holder: SearchMoviesViewHolder, position: Int) =
        holder.bindMovie(moviesList[position])

    override fun getItemCount() = moviesList.count()

    fun addItems(movies: List<Movie>) = this.moviesList.addAll(movies)

    fun clearItems() = this.moviesList.clear()

}