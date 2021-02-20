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
import com.example.filmstoday.models.cast.ActorFullInfoModel
import com.example.filmstoday.models.movie.Movie
import com.example.filmstoday.utils.ActorsBottomSheetBinder
import com.example.filmstoday.viewmodels.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SearchFragment : Fragment() {

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

    private val actorsAdapter = ActorsAdapter(object : OnActorCLickListener {
        override fun onItemCLick(actor: Actor) {
            enableActorBottomSheet(actor = actor)
            binding.actorBottomSheet.btnCLose.setOnClickListener {
                actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    })

    private val actorsBottomSheetBinder: ActorsBottomSheetBinder by lazy { ActorsBottomSheetBinder() }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var actorsBottomSheet: View
    private lateinit var actorsBottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(searchViewModel)
        initBottomSheets(view = view)
        setupSearchField()
        setupRecyclers()
        startObserving()
    }

    private fun initBottomSheets(view: View) {
        actorsBottomSheet = view.findViewById(R.id.actorBottomSheet)
        actorsBottomSheetBehavior = BottomSheetBehavior.from(actorsBottomSheet)
    }

    private fun setupSearchField() {
        binding.searchField.setOnFocusChangeListener { _, _ ->
            binding.searching = true
        }

        binding.searchField.doAfterTextChanged { editable ->
            searchViewModel.textChanged(editable.toString())
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclers() {
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
    }

    private fun enableActorBottomSheet(actor: Actor) {
        searchViewModel.searchActor(id = actor.id)
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

        searchViewModel.getActor().observe(viewLifecycleOwner, {
            fillActorInfo(actor = it)
            actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })
    }

    private fun fillActorInfo(actor: ActorFullInfoModel) {
        actorsBottomSheetBinder.bindActor(actorBottomSheet = binding.actorBottomSheet, actor = actor)
    }
}