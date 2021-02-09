package com.example.filmstoday.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.SearchMovieAdapter
import com.example.filmstoday.databinding.FragmentSearchBinding
import com.example.filmstoday.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchMovieAdapter: SearchMovieAdapter
    private lateinit var actorsAdapter: ActorsAdapter
    private val TAG = "SearchFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(searchViewModel)
        setupSearchField()
        setupRecyclers()
    }

    private fun setupSearchField() {
        binding.searchField.setOnFocusChangeListener { _, _ ->
            binding.appbar.setExpanded(false)
        }

        binding.searchField.doAfterTextChanged {
            searchViewModel.textChanged(query = it.toString())
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclers() {
        searchMovieAdapter = SearchMovieAdapter()
        actorsAdapter = ActorsAdapter()

        binding.rvMoviesSearchResult.apply {
            adapter = searchMovieAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        dividerItemDecoration.setDrawable(
            resources.getDrawable(
                R.drawable.separator,
                context?.theme
            )
        )
        binding.rvMoviesSearchResult.addItemDecoration(dividerItemDecoration)

        binding.rvActorsSearchResults.apply {
            adapter = actorsAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        startObserving()
    }

    private fun startObserving() {
        searchViewModel.getMovies().observe(viewLifecycleOwner, {
            searchMovieAdapter.clearItems()
            searchMovieAdapter.addItems(movies = it.results)
            searchMovieAdapter.notifyDataSetChanged()
            binding.textView2.visibility = View.VISIBLE
        })

        searchViewModel.getActors().observe(viewLifecycleOwner, {
            actorsAdapter.clearItems()
            actorsAdapter.addItems(actors = it.results)
            actorsAdapter.notifyDataSetChanged()
            binding.textView3.visibility = View.VISIBLE
        })
    }
}