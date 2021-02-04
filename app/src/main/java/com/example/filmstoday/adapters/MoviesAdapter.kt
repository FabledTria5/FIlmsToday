package com.example.filmstoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.models.Movie
import com.example.filmstoday.utils.Constants.Companion.POSTERS_BASE_URL
import com.squareup.picasso.Picasso

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val movies = arrayListOf<Movie>()

    inner class MoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.ivPoster)

        fun bindMovie(movie: Movie) = Picasso.get().load("$POSTERS_BASE_URL${movie.poster_path}").into(poster)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movies_list_item, parent, false)
        return MoviesViewHolder(itemView = itemView)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) = holder.bindMovie(movies[position])

    override fun getItemCount() = movies.size

    fun addItems(movies: List<Movie>) = this.movies.addAll(movies)

}