package com.example.filmstoday.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.models.movie.GenresModel
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.utils.ActorsBottomSheetBinder
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.getDuration
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.example.filmstoday.viewmodels.FullMovieViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso

class FullMovieFragment : Fragment() {

    private val actorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            enableActorBottomSheet(actor)
            binding.movieBottomSheet.actorBottomSheet.btnCLose.setOnClickListener {
                disableActorBottomSheet()
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
    private lateinit var actorsBottomSheet: View
    private lateinit var moviesBottomSheet: View
    private lateinit var actorsBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var moviesBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var currentMovie: MovieFullModel

    private val actorsBottomSheetBinder: ActorsBottomSheetBinder by lazy { ActorsBottomSheetBinder() }
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
        initBottomSheets(view)
        initRecyclerView()
        addListeners()
        setBackButtonBehavior(view)
    }

    private fun initBottomSheets(view: View) {
        actorsBottomSheet = view.findViewById(R.id.actorBottomSheet)
        moviesBottomSheet = view.findViewById(R.id.movieBottomSheet)
        actorsBottomSheetBehavior = BottomSheetBehavior.from(actorsBottomSheet)
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

    private fun disableActorBottomSheet() {
        actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        moviesBottomSheetBehavior.isDraggable = true
    }

    private fun enableActorBottomSheet(actor: Actor) {
        moviesBottomSheetBehavior.isDraggable = false
        actorsBottomSheetBehavior.isDraggable = false
        fullMovieViewModel.getActorInfo(actorId = actor.id)
    }

    private fun startObserve() {
        fullMovieViewModel.getObservedMovie().observe(viewLifecycleOwner, {
            fillMovieInfo(movie = it)
            currentMovie = it
            checkButtons(it.id)
        })

        fullMovieViewModel.getCast().observe(viewLifecycleOwner, {
            actorsAdapter.apply {
                addItems(actors = it.cast)
                notifyDataSetChanged()
            }
        })

        fullMovieViewModel.getObservingActor().observe(viewLifecycleOwner, {
            fillActorInfo(actor = it)
            actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        fullMovieViewModel.getComment(args.movieId).observe(viewLifecycleOwner, {
            it?.let {
                binding.movieBottomSheet.tvComment.text = it.text
                binding.movieBottomSheet.btnAddComment.visibility = View.INVISIBLE
            }
        })
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

    private fun setBackButtonBehavior(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (actorsBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        this.requestFocus()
                        return@OnKeyListener true
                    }
                }
                return@OnKeyListener false
            })
        }
    }

    private fun fillActorInfo(actor: ActorFullInfoModel) {
        actorsBottomSheetBinder.bindActor(
            actorBottomSheet = binding.movieBottomSheet.actorBottomSheet,
            actor = actor
        )
    }

    private fun fillMovieInfo(movie: MovieFullModel) {
        setPoster(posterPath = movie.poster_path)
        binding.movieBottomSheet.tvMovieName.text = movie.title
        binding.movieBottomSheet.tvReleaseYear.text = movie.release_date.take(4)
        binding.movieBottomSheet.tvReleaseCountry.text =
            fullMovieViewModel.getCountry(movie.production_countries)
        binding.movieBottomSheet.tvOverView.text = fullMovieViewModel.getDescription(movie.overview)
        binding.movieBottomSheet.tvDuration.text = getDuration(movie.runtime)
        binding.movieBottomSheet.tvRating.text = movie.vote_average.toString()
        setGenres(genres = movie.genres)
    }

    private fun setPoster(posterPath: String) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${posterPath}")
            .placeholder(R.drawable.ic_poster_placeholder)
            .into(binding.ivPosterFull)
    }

    private fun setGenres(genres: List<GenresModel>) {
        genresAdapter.apply {
            setGenres(genres = genres)
            notifyDataSetChanged()
        }
    }
}