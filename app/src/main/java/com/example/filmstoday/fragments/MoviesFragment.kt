package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmstoday.R
import com.example.filmstoday.adapters.MainMoviesAdapter
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.example.filmstoday.models.movie.MovieModel
import com.example.filmstoday.viewmodels.MoviesViewModel
import com.google.android.material.tabs.TabLayout

class MoviesFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by lazy {
        ViewModelProvider(this).get(MoviesViewModel::class.java)
    }

    private lateinit var binding: FragmentMoviesBinding

    private val mainMoviesAdapter = MainMoviesAdapter(object : OnMovieClickListener {
        override fun onItemClick(movieModel: MovieModel) {
            val action = MoviesFragmentDirections.openMovie(movieModel.id)
            requireView().findNavController().navigate(action)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_movies, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(moviesViewModel)
        doInitialization()
        setupTabListener()
    }

    private fun setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { moviesViewModel.changeTab(position = it.position) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun doInitialization() {
        binding.rvMoviesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainMoviesAdapter
        }
        startObserving()
    }

    private fun startObserving() {
        moviesViewModel.getObservedMovies().observe(viewLifecycleOwner, {
            binding.isLoading = true
            mainMoviesAdapter.clearItems()
            mainMoviesAdapter.addItems(movieModels = it.results)
            mainMoviesAdapter.notifyDataSetChanged()
            binding.isLoading = false
        })

        moviesViewModel.getPosition().observe(viewLifecycleOwner, {
            binding.tabLayout.getTabAt(it)?.select()
        })
    }
}