package com.example.filmstoday.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
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
import com.example.filmstoday.utils.Constants
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
    private lateinit var mSettings: SharedPreferences
    private var searchAdultContent: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettings = requireActivity().getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
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
        initBottomSheets(view = view)
        setupSearchField()
        setupRecyclers()
        startObserving()

        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (actorsBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        this.requestFocus()
                        return@OnKeyListener true
                    }
                }
                return@OnKeyListener false
            })
        }

        mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, false)
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
            searchAdultContent = mSettings.getBoolean(Constants.APP_PREFERENCE_ADULT_CONTENT, true)
            searchViewModel.textChanged(editable.toString(), searchAdultContent)
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
            searchMovieAdapter.apply {
                clearItems()
                addItems(movies = it.results)
                notifyDataSetChanged()
            }
            binding.textView2.visibility = View.VISIBLE
        })

        searchViewModel.getActors().observe(viewLifecycleOwner, {
            actorsAdapter.apply {
                clearItems()
                addItems(actors = it.results)
                notifyDataSetChanged()
            }
            binding.textView3.visibility = View.VISIBLE
        })

        searchViewModel.getActor().observe(viewLifecycleOwner, {
            fillActorInfo(actor = it)
            removeFocus(binding.searchField)
            actorsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })
    }

    private fun removeFocus(view: View) {
        view.clearFocus()
        requireView().requestFocus()
    }

    private fun fillActorInfo(actor: ActorFullInfoModel) {
        actorsBottomSheetBinder.bindActor(
            actorBottomSheet = binding.actorBottomSheet,
            actor = actor
        )
    }
}