package com.example.filmstoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.models.movie.MovieModel
import com.example.filmstoday.utils.Constants.POSTERS_BASE_URL_SMALL
import com.squareup.picasso.Picasso

class SearchMovieAdapter(private val OnMovieClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<SearchMovieAdapter.SearchMoviesViewHolder>() {

    private val moviesList = arrayListOf<MovieModel>()

    inner class SearchMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.ivSearchMoviePoster)

        fun bindMovie(movieModel: MovieModel) {
            Picasso.get().load("$POSTERS_BASE_URL_SMALL${movieModel.poster_path}")
                .placeholder(R.drawable.ic_poster_placeholder)
                .into(poster)

            itemView.setOnClickListener {
                OnMovieClickListener.onItemClick(movieModel = movieModel)
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

    fun addItems(movieModels: List<MovieModel>) = this.moviesList.addAll(movieModels)

    fun clearItems() = this.moviesList.clear()

}