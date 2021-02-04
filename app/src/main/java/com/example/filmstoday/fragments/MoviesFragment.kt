package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmstoday.BuildConfig
import com.example.filmstoday.R
import com.example.filmstoday.adapters.MoviesAdapter
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.example.filmstoday.models.Movie
import com.example.filmstoday.viewmodels.MoviesViewModel

class MoviesFragment : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter

    private val LOG_TAG = "MoviesFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        binding.viewModel = moviesViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doInitialization()
    }

    private fun doInitialization() {
        moviesAdapter = MoviesAdapter()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = moviesAdapter
        getPopularMovies()
    }

    private fun getPopularMovies() {
        binding.isLoading = true
        moviesViewModel.getPopularMovies(BuildConfig.MOVIES_API_KEY).observe(viewLifecycleOwner, {
            binding.isLoading = false
            if (it != null) {
                moviesAdapter.addItems(it.results)
                moviesAdapter.notifyDataSetChanged()
            }
        })
    }


}