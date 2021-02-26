package com.example.filmstoday.utils

import com.example.filmstoday.R
import com.example.filmstoday.databinding.ActorBottomSheetBinding
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.squareup.picasso.Picasso

class ActorsBottomSheetBinder {

    fun bindActor(actorBottomSheet: ActorBottomSheetBinding, actor: ActorFullInfoModel) {
        actorBottomSheet.tvActorFullName.text = actor.name
        actorBottomSheet.tvActorBiography.text = actor.biography
        actorBottomSheet.tvBirthday.text = actor.birthday
        actorBottomSheet.tvPlaceOfBirth.text = actor.placeOfBirth
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${actor.photo}")
            .placeholder(R.drawable.photo_placeholder)
            .into(actorBottomSheet.ivActorFullPhoto)
    }
    
}
