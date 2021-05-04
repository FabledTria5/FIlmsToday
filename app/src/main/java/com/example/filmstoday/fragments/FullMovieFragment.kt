package com.example.filmstoday.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.GenresAdapter
import com.example.filmstoday.adapters.listeners.OnActorCLickListener
import com.example.filmstoday.databinding.FragmentFullMovieBinding
import com.example.filmstoday.models.cast.Actor
import com.example.filmstoday.models.movie.GenresModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.videos.VideosBase
import com.example.filmstoday.utils.Constants.YOUTUBE_BASE_URL
import com.example.filmstoday.utils.getDuration
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.example.filmstoday.viewmodels.factories.FullMovieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton

class FullMovieFragment : Fragment() {

    private val actorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            FullMovieFragmentDirections.openActorFromMovie(actor.id).also {
                requireView().findNavController().navigate(it)
            }
        }
    })

    private val fullMovieViewModel: FullMovieViewModel by viewModels {
        FullMovieViewModelFactory(
            application = requireActivity().application
        )
    }

    private lateinit var binding: FragmentFullMovieBinding
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var moviesBottomSheet: View
    private lateinit var moviesBottomSheetBehavior: BottomSheetBehavior<View>

    private val args: FullMovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_full_movie, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(fullMovieViewModel)
        fullMovieViewModel.getReceivedMovieInfo(args.movieId)
        startObserve()
        initBottomSheet(view)
        initRecyclerView()
        addListeners()
    }

    private fun initBottomSheet(view: View) {
        moviesBottomSheet = view.findViewById(R.id.movieBottomSheet)
        moviesBottomSheetBehavior = BottomSheetBehavior.from(moviesBottomSheet)
    }

    private fun initRecyclerView() {
        genresAdapter = GenresAdapter()

        binding.movieBottomSheet.rvMovieCast.apply {
            adapter = actorsAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        binding.movieBottomSheet.rvGenres.apply {
            adapter = genresAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }.run {
            val dividerItemDecoration =
                DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
            AppCompatResources.getDrawable(requireContext(), R.drawable.separator)?.let {
                dividerItemDecoration.setDrawable(
                    it
                )
            }
            this.addItemDecoration(dividerItemDecoration)
        }
    }

    private fun startObserve() {
        fullMovieViewModel.getObservedMovie().observe(viewLifecycleOwner) {
            fillMovieInfo(movie = it)
            binding.currentMovie = it
        }

        fullMovieViewModel.observeWantBtn(args.movieId).observe(viewLifecycleOwner) {
            toggleBtn(button = binding.movieBottomSheet.btnWant, it)
        }

        fullMovieViewModel.observeWatchedBtn(args.movieId).observe(viewLifecycleOwner) {
            toggleBtn(button = binding.movieBottomSheet.btnWatched, it)
        }

        fullMovieViewModel.getCast().observe(viewLifecycleOwner) {
            actorsAdapter.apply {
                addItems(actors = it.cast)
                notifyDataSetChanged()
            }
        }

        fullMovieViewModel.getObservingVideos().observe(viewLifecycleOwner) {
            enableVideoButton(it)
        }
    }

    private fun toggleBtn(button: MaterialButton, isActive: Boolean) {
        if (isActive) button.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        else
            button.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.elementsColor
                )
            )
    }

    private fun enableVideoButton(videosList: VideosBase) {
        binding.apply {
            isVideosAvailable = videosList.results.isNotEmpty()
            btnOpenVideo.setOnClickListener {
                requireContext().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("$YOUTUBE_BASE_URL${videosList.results[0].key}")
                    )
                )
            }
        }
    }

    private fun addListeners() {
        binding.movieBottomSheet.btnWant.setOnClickListener {
            fullMovieViewModel.addMovieToWant(binding.currentMovie)
        }

        binding.movieBottomSheet.btnWatched.setOnClickListener {
            fullMovieViewModel.addMovieToWatched(binding.currentMovie)
        }
    }

    private fun fillMovieInfo(movie: MovieFullModel) {
        binding.movieBottomSheet.tvDuration.text = getDuration(movie.runtime)
        setGenres(genres = movie.genres)
    }

    private fun setGenres(genres: List<GenresModel>) {
        genresAdapter.apply {
            setGenres(genres = genres)
            notifyDataSetChanged()
        }
    }
}