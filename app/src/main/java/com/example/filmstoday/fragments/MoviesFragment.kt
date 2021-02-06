package com.example.filmstoday.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmstoday.R
import com.example.filmstoday.adapters.MainMoviesAdapter
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.example.filmstoday.viewmodels.MoviesViewModel
import com.google.android.material.tabs.TabLayout

class MoviesFragment : Fragment() {

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var mainMoviesAdapter: MainMoviesAdapter

    private val TAG = "MoviesFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        moviesViewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
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
                moviesViewModel.changeTab(position = tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun doInitialization() {
        mainMoviesAdapter = MainMoviesAdapter()
        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = mainMoviesAdapter
        startObserving()
    }

    private fun startObserving() {
        moviesViewModel.getObservedMovies().observe(viewLifecycleOwner, {
            binding.isLoading = true
            mainMoviesAdapter.clearItems()
            mainMoviesAdapter.addItems(it.results)
            mainMoviesAdapter.notifyDataSetChanged()
            binding.isLoading = false
        })

        moviesViewModel.getPosition().observe(viewLifecycleOwner, {
            val tab = binding.tabLayout.getTabAt(it)
            tab?.select()
        })
    }
}