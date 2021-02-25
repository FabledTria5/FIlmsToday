package com.example.filmstoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.models.movie.Movie
import com.example.filmstoday.utils.Constants.Companion.POSTERS_BASE_URL
import com.squareup.picasso.Picasso

class MainMoviesAdapter(private var OnMovieClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<MainMoviesAdapter.MoviesViewHolder>() {

    private val moviesList = arrayListOf<Movie>()

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.ivPoster)

        fun bindMovie(movie: Movie) {
            Picasso.get().load("$POSTERS_BASE_URL${movie.poster_path}").into(poster)
            itemView.setOnClickListener {
                OnMovieClickListener.onItemClick(movie = movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MoviesViewHolder(
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movies_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) =
        holder.bindMovie(moviesList[position])

    override fun getItemCount() = moviesList.count()

    fun addItems(movies: List<Movie>) = this.moviesList.addAll(movies)

    fun clearItems() = this.moviesList.clear()

}