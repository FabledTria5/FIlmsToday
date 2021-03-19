package com.example.filmstoday.fragments

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.filmstoday.R
import com.example.filmstoday.activities.MapsActivity
import com.example.filmstoday.databinding.ActorBottomSheetBinding
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.selectText
import com.example.filmstoday.viewmodels.ActorViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActorDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ActorBottomSheetBinding

    private val args: ActorDialogFragmentArgs by navArgs()
    private val actorViewModel: ActorViewModel by lazy {
        ViewModelProvider(this).get(ActorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.actor_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(actorViewModel)
        getActorInfo()
        startObserve()
        addListeners()
    }

    private fun getActorInfo() = actorViewModel.getActorInfo(args.actorId)

    private fun startObserve() {
        actorViewModel.getObservedActor().observe(viewLifecycleOwner) {
            binding.currentActor = it
        }

        actorViewModel.getFavorite(args.actorId).observe(viewLifecycleOwner) {
            binding.isFavorite = it
        }
    }

    private fun addListeners() {
        binding.btnAddToFavorite.setOnClickListener {
            actorViewModel.apply {
                when (binding.isFavorite) {
                    true -> binding.currentActor?.id?.let { it1 -> removeActorFromFavorite(it1) }
                    else -> binding.currentActor?.let { addActorToFavorite(it) }
                }
            }
        }

        binding.tvPlaceOfBirth.apply {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                selectText(
                    binding.tvPlaceOfBirth,
                    requireContext()
                )
                Intent(activity, MapsActivity::class.java).also {
                    it.putExtra(Constants.ACTOR_PLACE_OF_BIRTH, binding.currentActor?.placeOfBirth)
                    it.putExtra(Constants.ACTOR_NAME, binding.currentActor?.name)
                    it.putExtra(Constants.ACTOR_PHOTO, binding.currentActor?.photo)
                    context.startActivity(it)
                }
            }
        }
    }
}