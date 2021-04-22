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
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.MainMoviesAdapter
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.example.filmstoday.models.movie.MovieModel
import com.example.filmstoday.utils.hide
import com.example.filmstoday.utils.show
import com.example.filmstoday.viewmodels.MoviesViewModel
import com.google.android.material.tabs.TabLayout

class MoviesFragment : Fragment() {

    private val moviesViewModel: MoviesViewModel by lazy {
        ViewModelProvider(this).get(MoviesViewModel::class.java)
    }

    private lateinit var binding: FragmentMoviesBinding

    private var currentPage = 1
    private val totalAvailablePages = 1000

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
                currentPage = 1
                tab?.let { getFilms() }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun doInitialization() {
        binding.rvMoviesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainMoviesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1)) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1
                            getFilms()
                        }
                    }
                }
            })
        }
        getFilms()
    }

    private fun getFilms() {
        binding.progressBar.show()
        moviesViewModel.getObservedMovies(currentPage, binding.tabLayout.selectedTabPosition)
            .observe(viewLifecycleOwner, {
                if (mainMoviesAdapter.itemCount > 0) {
                    mainMoviesAdapter.addItems(movieModels = it.results)
                    mainMoviesAdapter
                        .notifyItemRangeChanged(0, mainMoviesAdapter.itemCount)
                } else {
                    mainMoviesAdapter.addItems(movieModels = it.results)
                    mainMoviesAdapter.notifyDataSetChanged()
                }
                binding.progressBar.hide()
            })

        moviesViewModel.getPosition().observe(viewLifecycleOwner, {
            binding.tabLayout.getTabAt(it)?.select()
        })
    }
}