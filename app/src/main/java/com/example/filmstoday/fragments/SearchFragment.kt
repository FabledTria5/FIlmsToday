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

    private val actorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            SearchFragmentDirections.openActorFromSearch(actor.id).also {
                requireView().findNavController().navigate(it)
            }
        }
    })

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mSettings: SharedPreferences

    private val searchAdultContent: Boolean by lazy {
        mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings =
            requireActivity().getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(searchViewModel)
        setupSearchField()
        setupRecyclers()
        startObserving()

        mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, false)
    }

    private fun setupSearchField() {
        binding.searchField.setOnFocusChangeListener { _, _ ->
            binding.searching = true
        }

        binding.searchField.doAfterTextChanged { editable ->
            searchViewModel.textChanged(editable.toString(), searchAdultContent)
        }
    }


    private fun setupRecyclers() {
        binding.rvMoviesSearchResult.apply {
            adapter = searchMovieAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL)
        ResourcesCompat.getDrawable(resources, R.drawable.separator, requireActivity().theme)?.let {
            dividerItemDecoration.setDrawable(
                it
            )
        }
        binding.rvMoviesSearchResult.addItemDecoration(dividerItemDecoration)

        binding.rvActorsSearchResults.apply {
            adapter = actorsAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun startObserving() {
        searchViewModel.getMovies().observe(viewLifecycleOwner, {
            searchMovieAdapter.apply {
                clearItems()
                addItems(movieModels = it.results)
                notifyDataSetChanged()
            }
            binding.foundMovies = it.results.isNotEmpty()
        })

        searchViewModel.getActors().observe(viewLifecycleOwner, {
            actorsAdapter.apply {
                clearItems()
                addItems(actors = it.results)
                notifyDataSetChanged()
            }
            binding.foundActors = it.results.isNotEmpty()
        })
    }
}