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
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.showSnackBar
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class FullMovieFragment : Fragment() {

    private val actorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            val actorsBottomSheetBehavior = BottomSheetBehavior.from(actorsBottomSheet)
            val moviesBottomSheetBehavior = BottomSheetBehavior.from(moviesBottomSheet)

            moviesBottomSheetBehavior.isDraggable = false
            actorsBottomSheetBehavior.isDraggable = false
            actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            fullMovieViewModel.getActorInfo(actorId = actor.id)

            binding.movieBottomSheet.actorBottomSheet.btnCLose.setOnClickListener {
                actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                moviesBottomSheetBehavior.isDraggable = true
            }
        }
    })

    private lateinit var fullMovieViewModel: FullMovieViewModel
    private lateinit var binding: FragmentFullMovieBinding
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var actorsBottomSheet: View
    private lateinit var moviesBottomSheet: View
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
        Handler(Looper.getMainLooper()).postDelayed({startObserve()}, 300)
        initRecyclerView()
        addListeners()
        actorsBottomSheet = view.findViewById(R.id.actorBottomSheet)
        moviesBottomSheet = view.findViewById(R.id.movieBottomSheet)
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
        fullMovieViewModel.getObservedMovie().observe(viewLifecycleOwner, {
            fillMovieInfo(it)
        })

        fullMovieViewModel.getCast().observe(viewLifecycleOwner, {
            actorsAdapter.apply {
                addItems(it.cast)
                notifyDataSetChanged()
            }
        })

        fullMovieViewModel.getObservingActor().observe(viewLifecycleOwner, {
            fillActorInfo(it)
        })
    }

    private fun addListeners() {
        binding.movieBottomSheet.btnWant.setOnClickListener {
            it.apply {
                showSnackBar(R.string.added_to_wanted, Snackbar.LENGTH_SHORT)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }

        binding.movieBottomSheet.btnWatched.setOnClickListener {
            it.apply {
                showSnackBar(R.string.added_to_watched, Snackbar.LENGTH_SHORT)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            }
        }
    }

    private fun fillActorInfo(it: ActorFullInfoModel) {
        binding.movieBottomSheet.actorBottomSheet.tvActorFullName.text = it.name
        binding.movieBottomSheet.actorBottomSheet.tvActorBiography.text = it.biography
        binding.movieBottomSheet.actorBottomSheet.tvBirthday.text = it.birthday
    }

    private fun fillMovieInfo(it: MovieFullModel) {
        setPoster(posterPath = it.poster_path)
        setTitle(title = it.title)
        setDate(date = it.release_date)
        setCountry(name = fullMovieViewModel.getCountry(it.production_countries))
        setDescription(overview = fullMovieViewModel.getDescription(it.overview))
        setDuration(duration = fullMovieViewModel.getDuration(it.runtime))
        setGenres(genres = it.genres)
        setRating(rating = it.vote_average.toString())
    }

    private fun setPoster(posterPath: String) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${posterPath}")
            .placeholder(R.drawable.ic_poster_placeholder)
            .into(binding.ivPosterFull)
    }

    private fun setTitle(title: String) {
        binding.movieBottomSheet.tvMovieName.text = title
    }

    private fun setDate(date: String) {
        binding.movieBottomSheet.tvReleaseYear.text = fullMovieViewModel.convertDate(date)
    }

    private fun setCountry(name: String) {
        binding.movieBottomSheet.tvReleaseCountry.text = name
    }

    private fun setDescription(overview: String) {
        binding.movieBottomSheet.tvOverView.text = overview
    }

    private fun setDuration(duration: String) {
        binding.movieBottomSheet.tvDuration.text = duration
    }

    private fun setGenres(genres: List<Genres>) {
        genresAdapter.apply {
            setGenres(genres = genres)
            notifyDataSetChanged()
        }
    }

    private fun setRating(rating: String) {
        binding.movieBottomSheet.tvRating.text = rating
    }
}