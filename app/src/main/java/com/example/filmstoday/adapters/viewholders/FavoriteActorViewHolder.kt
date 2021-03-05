package com.example.filmstoday.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.data.FavoriteActor
import com.example.filmstoday.utils.Constants
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class FavoriteActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val photo = itemView.findViewById<RoundedImageView>(R.id.rivActorPhoto)
    private val actorName = itemView.findViewById<TextView>(R.id.tvActorName)
    private val placeOfBirth = itemView.findViewById<TextView>(R.id.tvActorPlaceOfBirth)

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context).inflate(R.layout.favorite_actor_item, parent, false)
    )

    fun bind(favoriteActor: FavoriteActor) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${favoriteActor.actorPosterPath}")
            .placeholder(R.drawable.photo_placeholder)
            .into(photo)
        actorName.text = favoriteActor.actorName
        placeOfBirth.text = favoriteActor.placeOfBirth
    }
}