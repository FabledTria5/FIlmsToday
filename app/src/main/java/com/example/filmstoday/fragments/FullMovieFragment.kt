package com.example.filmstoday.fragments

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.GenresAdapter
import com.example.filmstoday.adapters.listeners.OnActorCLickListener
import com.example.filmstoday.databinding.FragmentFullMovieBinding
import com.example.filmstoday.interactors.StringInteractorImpl
import com.example.filmstoday.models.cast.Actor
import com.example.filmstoday.models.movie.GenresModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.models.videos.VideosBase
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.Constants.Companion.YOUTUBE_BASE_URL
import com.example.filmstoday.utils.getDuration
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.example.filmstoday.viewmodels.factories.FullMovieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso

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
            stringInteractor = StringInteractorImpl(requireContext()),
            application = requireActivity().application
        )
    }

    private lateinit var binding: FragmentFullMovieBinding
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var moviesBottomSheet: View
    private lateinit var moviesBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var currentMovie: MovieFullModel

    private val args: FullMovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            currentMovie = it
            checkButtons(it.id)
        }

        fullMovieViewModel.getCast().observe(viewLifecycleOwner) {
            actorsAdapter.apply {
                addItems(actors = it.cast)
                notifyDataSetChanged()
            }
        }

        fullMovieViewModel.getComment(args.movieId).observe(viewLifecycleOwner) {
            it?.let {
                binding.movieBottomSheet.tvComment.text = it.text
                binding.movieBottomSheet.btnAddComment.visibility = View.INVISIBLE
            }
        }

        fullMovieViewModel.getObservingVideos().observe(viewLifecycleOwner) {
            enableVideoButton(it)
        }
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

    private fun checkButtons(id: Int) {
        if (fullMovieViewModel.checkWantBtn(id = id)) {
            binding.movieBottomSheet.btnWant.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }

        if (fullMovieViewModel.checkWatchedBtn(id = id)) {
            binding.movieBottomSheet.btnWatched.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }
    }

    private fun addListeners() {
        binding.movieBottomSheet.btnWant.setOnClickListener {
            fullMovieViewModel.addMovieToWant(currentMovie)
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        binding.movieBottomSheet.btnWatched.setOnClickListener {
            fullMovieViewModel.addMovieToWatched(currentMovie)
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        binding.movieBottomSheet.btnAddComment.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        val customView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val builder =
            AlertDialog.Builder(context, R.style.MaterialAlertDialog_MaterialComponents_Title_Icon)
                .setView(customView)
                .setCancelable(false)
                .setTitle(R.string.leave_a_comment)
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    run {
                        val editText = customView.findViewById<TextInputEditText>(R.id.commentField)
                        fullMovieViewModel.saveComment(currentMovie.id, editText.text.toString())
                    }
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun fillMovieInfo(movie: MovieFullModel) {
        binding.movieBottomSheet.currentMovie = movie
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