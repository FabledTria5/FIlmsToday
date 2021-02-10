package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.GenresAdapter
import com.example.filmstoday.databinding.FragmentFullMovieBinding
import com.example.filmstoday.interactors.StringInteractorImpl
import com.example.filmstoday.models.movie.Genres
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.viewmodels.FullMovieViewModel
import com.squareup.picasso.Picasso

class FullMovieFragment : Fragment() {

    private lateinit var fullMovieViewModel: FullMovieViewModel
    private lateinit var binding: FragmentFullMovieBinding
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var genresAdapter: GenresAdapter
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
        startObserve()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        actorsAdapter = ActorsAdapter()
        genresAdapter = GenresAdapter()

        binding.bottomSheet.rvMovieCast.apply {
            adapter = actorsAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        binding.bottomSheet.rvGenres.apply {
            adapter = genresAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        AppCompatResources.getDrawable(requireContext(), R.drawable.separator)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        binding.bottomSheet.rvGenres.addItemDecoration(dividerItemDecoration)
    }

    private fun startObserve() {
        fullMovieViewModel.getObservedMovie().observe(viewLifecycleOwner, {
            setPoster(posterPath = it.poster_path)
            setTitle(title = it.title)
            setDate(date = it.release_date)
            setCountry(name = fullMovieViewModel.getCountry(it.production_countries))
            setDescription(overview = fullMovieViewModel.getDescription(it.overview))
            setDuration(duration = fullMovieViewModel.getDuration(it.runtime))
            setGenres(it.genres)
            setRating(it.vote_average.toString())
        })

        fullMovieViewModel.getCast().observe(viewLifecycleOwner, {
            actorsAdapter.addItems(it.cast)
            actorsAdapter.notifyDataSetChanged()
        })
    }

    private fun setPoster(posterPath: String) {
        Picasso.get().load("${Constants.POSTERS_BASE_URL}${posterPath}")
            .placeholder(R.drawable.ic_poster_placeholder)
            .into(binding.ivPosterFull)
    }

    private fun setTitle(title: String) {
        binding.bottomSheet.tvMovieName.text = title
    }

    private fun setDate(date: String) {
        binding.bottomSheet.tvReleaseYear.text = fullMovieViewModel.convertDate(date)
    }

    private fun setCountry(name: String) {
        binding.bottomSheet.tvReleaseCountry.text = name
    }

    private fun setDescription(overview: String) {
        binding.bottomSheet.tvOverView.text = overview
    }

    private fun setDuration(duration: String) {
        binding.bottomSheet.tvDuration.text = duration
    }

    private fun setGenres(genres: List<Genres>) {
        genresAdapter.setGenres(genres = genres)
        genresAdapter.notifyDataSetChanged()
    }

    private fun setRating(rating: String) {
        binding.bottomSheet.tvRating.text = rating
    }
}