package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.filmstoday.R
import com.example.filmstoday.databinding.ActorBottomSheetBinding
import com.example.filmstoday.interactors.StringInteractorImpl
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.viewmodels.ActorViewModel
import com.example.filmstoday.viewmodels.factories.ActorViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class ActorDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ActorBottomSheetBinding

    private val args: ActorDialogFragmentArgs by navArgs()
    private val actorViewModel: ActorViewModel by viewModels {
        ActorViewModelFactory(
            stringInteractor = StringInteractorImpl(requireContext()),
            application = requireActivity().application
        )
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
            setActorPhoto(it.photo)
        }

        actorViewModel.getFavorite(args.actorId).observe(viewLifecycleOwner) {
            binding.isFavorite = it
        }
    }

    private fun addListeners() {
        binding.btnAddToFavorite.setOnClickListener {
            actorViewModel.triggerFavorite(args.actorId)
        }
    }

    private fun setActorPhoto(photo: String?) =
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${photo}")
            .placeholder(R.drawable.photo_placeholder)
            .into(binding.ivActorFullPhoto)
}