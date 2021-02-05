package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmstoday.R
import com.example.filmstoday.adapters.MoviesAdapter
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.example.filmstoday.models.Movie
import com.example.filmstoday.responses.MoviesResponse
import com.example.filmstoday.viewmodels.MoviesViewModel
import com.google.android.material.tabs.TabLayout

class MoviesFragment : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var moviesAdapter: MoviesAdapter

    private val TAG = "MoviesFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        binding.viewModel = moviesViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(moviesViewModel)
        doInitialization()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                moviesViewModel.changeTab(position = tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun doInitialization() {
        moviesAdapter = MoviesAdapter()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = moviesAdapter
        startObserve()
    }

    private fun startObserve() {
        binding.isLoading = true
        moviesViewModel.getObservedMovies().observe(viewLifecycleOwner, {
            binding.isLoading = false
            if (it != null) {
                moviesAdapter.clearItems()
                moviesAdapter.addItems(it.results)
                moviesAdapter.notifyDataSetChanged()
            }
        })

        moviesViewModel.getPosition().observe(viewLifecycleOwner, {
            val tab = binding.tabLayout.getTabAt(it)
            tab?.select()
        })
    }
}