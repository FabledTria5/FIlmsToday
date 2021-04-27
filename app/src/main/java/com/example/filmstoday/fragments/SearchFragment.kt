package com.example.filmstoday.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.SearchMovieAdapter
import com.example.filmstoday.adapters.listeners.OnActorCLickListener
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.databinding.FragmentSearchBinding
import com.example.filmstoday.models.cast.Actor
import com.example.filmstoday.models.movie.MovieModel
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val searchMovieAdapter = SearchMovieAdapter(object : OnMovieClickListener {
        override fun onItemClick(movieModel: MovieModel) {
            val action =
                SearchFragmentDirections.openFullMovie(movieModel.id)
            requireView().findNavController().navigate(action)
        }
    })

    private val searchActorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            SearchFragmentDirections.openActorFromSearch(actor.id).also {
                requireView().findNavController().navigate(it)
            }
        }
    })

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mSettings: SharedPreferences

    private var actorsCurrentPage = 1
    private var moviesCurrentPage = 1

    private val searchAdultContent: Boolean by lazy {
        mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings =
            requireActivity().getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(searchViewModel)
        setupSearchField()
        setupRecyclers()
        mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, false)
    }

    private fun setupSearchField() {
        binding.searchField.setOnFocusChangeListener { _, _ ->
            binding.searching = true
        }

        binding.searchField.doAfterTextChanged { editable ->
            searchActorsAdapter.clearItems()
            searchMovieAdapter.clearItems()
            getActors(editable.toString())
            getMovies(editable.toString())
        }
    }


    private fun setupRecyclers() {
        binding.rvMoviesSearchResult.apply {
            adapter = searchMovieAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val dividerItemDecoration =
                DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
            ResourcesCompat.getDrawable(resources, R.drawable.separator, requireActivity().theme)
                ?.let {
                    dividerItemDecoration.setDrawable(
                        it
                    )
                }
            addItemDecoration(dividerItemDecoration)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollHorizontally(RecyclerView.LAYOUT_DIRECTION_RTL)) {
                        moviesCurrentPage += 1
                        getMovies("")
                    }
                }
            })
        }

        binding.rvActorsSearchResults.apply {
            adapter = searchActorsAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollHorizontally(RecyclerView.LAYOUT_DIRECTION_RTL)) {
                        moviesCurrentPage += 1
                        getActors("")
                    }
                }
            })
        }
    }

    private fun getActors(query: String) {
        searchViewModel.getActors(actorsCurrentPage, query).observe(viewLifecycleOwner, {
            searchActorsAdapter.apply {
                if (itemCount > 0) {
                    addItems(actors = it.results)
                    notifyItemRangeChanged(0, itemCount)
                } else {
                    addItems(actors = it.results)
                    notifyDataSetChanged()
                }
            }
            binding.foundActors = it.results.isNotEmpty()
        })
    }

    private fun getMovies(query: String) {
        searchViewModel.getMovies(moviesCurrentPage, query, searchAdultContent)
            .observe(viewLifecycleOwner, {
                searchMovieAdapter.apply {
                    if (itemCount > 0) {
                        addItems(movieModels = it.results)
                        notifyItemRangeChanged(0, itemCount)
                    } else {
                        addItems(movieModels = it.results)
                        notifyDataSetChanged()
                    }
                }
                binding.foundMovies = it.results.isNotEmpty()
            })
    }
}