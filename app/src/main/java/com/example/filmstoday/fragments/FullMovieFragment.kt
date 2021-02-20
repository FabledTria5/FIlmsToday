package com.example.filmstoday.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.example.filmstoday.models.movie.Genres
import com.example.filmstoday.models.movie.MovieFullModel
import com.example.filmstoday.utils.ActorsBottomSheetBinder
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
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

    private lateinit var fullMovieViewModel: FullMovieViewModel
    private lateinit var binding: FragmentFullMovieBinding
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var actorsBottomSheet: View
    private lateinit var moviesBottomSheet: View
    private lateinit var actorsBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var moviesBottomSheetBehavior: BottomSheetBehavior<View>

    private val actorsBottomSheetBinder: ActorsBottomSheetBinder by lazy { ActorsBottomSheetBinder() }
    private val args: FullMovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_full_movie, container, false)
        fullMovieViewModel =
            FullMovieViewModel(stringInteractor = StringInteractorImpl(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(fullMovieViewModel)
        fullMovieViewModel.getReceivedMovieInfo(args.movieId)
        Handler(Looper.getMainLooper()).postDelayed({ startObserve() }, 500)
        initBottomSheets(view)
        initRecyclerView()
        addListeners()
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
    }

    private fun addListeners() {
        binding.movieBottomSheet.btnWant.setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        binding.movieBottomSheet.btnWatched.setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
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
        binding.movieBottomSheet.tvReleaseYear.text =
            fullMovieViewModel.convertDate(date = movie.release_date)
        binding.movieBottomSheet.tvReleaseCountry.text =
            fullMovieViewModel.getCountry(productionCountries = movie.production_countries)
        binding.movieBottomSheet.tvOverView.text = movie.overview
        binding.movieBottomSheet.tvDuration.text =
            fullMovieViewModel.getDuration(runtime = movie.runtime)
        binding.movieBottomSheet.tvRating.text = movie.vote_average.toString()
        setGenres(genres = movie.genres)
    }

    private fun setPoster(posterPath: String) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${posterPath}")
            .placeholder(R.drawable.ic_poster_placeholder)
            .into(binding.ivPosterFull)
    }

    private fun setGenres(genres: List<Genres>) {
        genresAdapter.apply {
            setGenres(genres = genres)
            notifyDataSetChanged()
        }
    }
}