package com.example.filmstoday.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.adapters.viewholders.DoubleGridViewHolder
import com.example.filmstoday.adapters.viewholders.SimpleItemViewHolder
import com.example.filmstoday.adapters.viewholders.TripleGridViewHolder
import com.example.filmstoday.models.movie.SimpleMovie

class ProfileMoviesAdapter(private val layoutManager: GridLayoutManager? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        SMALL,
        MEDIUM,
        BIG
    }

    private val moviesList = arrayListOf<SimpleMovie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.SMALL.ordinal -> SimpleItemViewHolder(parent = parent)
            ViewType.MEDIUM.ordinal -> DoubleGridViewHolder(parent = parent)
            else -> TripleGridViewHolder(parent = parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SimpleItemViewHolder -> holder.bind(movieFullModel = moviesList[position])
            is DoubleGridViewHolder -> holder.bind(movieFullModel = moviesList[position])
            is TripleGridViewHolder -> holder.bind(movieFullModel = moviesList[position])
            else -> throw RuntimeException("Unknown ViewHolder")
        }
    }

    override fun getItemCount() = moviesList.count()

    override fun getItemViewType(position: Int): Int {
        return when (layoutManager?.spanCount) {
            1 -> ViewType.SMALL.ordinal
            2 -> ViewType.MEDIUM.ordinal
            else -> ViewType.BIG.ordinal
        }
    }

    fun addMovies(movies: List<SimpleMovie>) = moviesList.addAll(movies)

    fun clearMovies() = moviesList.clear()

}