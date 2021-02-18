package com.example.filmstoday.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ActorsAdapter
import com.example.filmstoday.adapters.listeners.OnMovieClickListener
import com.example.filmstoday.adapters.SearchMovieAdapter
import com.example.filmstoday.databinding.FragmentSearchBinding
import com.example.filmstoday.models.movie.Movie
import com.example.filmstoday.responses.ActorsResponse
import com.example.filmstoday.service.ActorsService
import com.example.filmstoday.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private val loadActorsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val actorsResponse = intent?.getSerializableExtra("actorsResponse") as ActorsResponse
            actorsAdapter.clearItems()
            actorsAdapter.addItems(actors = actorsResponse.results)
            actorsAdapter.notifyDataSetChanged()
        }
    }

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val searchMovieAdapter = SearchMovieAdapter(object : OnMovieClickListener {
        override fun onItemClick(movie: Movie) {
            val action =
                SearchFragmentDirections.openFullMovie(movie.id)
            requireView().findNavController().navigate(action)
        }
    })

    private lateinit var binding: FragmentSearchBinding
    private lateinit var actorsAdapter: ActorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadActorsReceiver, IntentFilter("DETAILS INTENT FILTER"))
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun setupSearchField() {
        binding.searchField.setOnFocusChangeListener { _, _ ->
            binding.appbar.setExpanded(false)
        }

        binding.searchField.doAfterTextChanged { editable ->
            searchViewModel.textChanged(editable.toString())
            context?.let { context ->
                context.startService(Intent(context, ActorsService::class.java).apply {
                    putExtra("ActorName", this@SearchFragment.binding.searchField.text.toString())
                })
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclers() {
//        actorsAdapter = ActorsAdapter()

        binding.rvMoviesSearchResult.apply {
            adapter = searchMovieAdapter
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
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
    }
}