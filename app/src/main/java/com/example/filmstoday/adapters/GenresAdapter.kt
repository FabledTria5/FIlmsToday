package com.example.filmstoday.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.models.movie.GenresModel

class GenresAdapter : RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

    private val genresList = arrayListOf<GenresModel>()

    inner class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(genre: GenresModel) {
            itemView.findViewById<TextView>(R.id.genreName).text = genre.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GenresViewHolder(
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.genres_item, parent, false)
    )

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) =
        holder.bind(genre = genresList[position])

    override fun getItemCount() = genresList.count()

    fun setGenres(genres: List<GenresModel>) = genresList.addAll(genres)

}