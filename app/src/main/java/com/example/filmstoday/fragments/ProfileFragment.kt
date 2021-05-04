package com.example.filmstoday.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ProfileMoviesAdapter
import com.example.filmstoday.adapters.listeners.OnFavoriteActorClickListener
import com.example.filmstoday.adapters.listeners.OnSimpleMovieClickListener
import com.example.filmstoday.data.FavoriteActor
import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie
import com.example.filmstoday.databinding.FragmentProfileBinding
import com.example.filmstoday.models.movie.SimpleMovie
import com.example.filmstoday.utils.*
import com.example.filmstoday.utils.Constants.APP_PREFERENCE
import com.example.filmstoday.utils.Constants.APP_PREFERENCE_FILTERING_OPTION
import com.example.filmstoday.utils.Constants.STANDARD_FILTER_OPTION
import com.example.filmstoday.viewmodels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private var listLayoutManager: GridLayoutManager? = null
    private lateinit var wantMovies: List<WantMovie>
    private lateinit var watchedMovies: List<WatchedMovie>
    private lateinit var favoriteActors: List<FavoriteActor>

    private lateinit var profileMoviesAdapter: ProfileMoviesAdapter
    private lateinit var filterAdapter: ArrayAdapter<String>
    private lateinit var binding: FragmentProfileBinding
    private lateinit var filterBottomSheet: View
    private lateinit var filterBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mSettings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(profileViewModel)
        doInitialization(view = view)
        setBackButtonBehavior(view = view)
        setupListeners()
        setupRecyclerView()
        setupTouchHelper()
        startObserve()
    }

    private fun doInitialization(view: View) {
        filterBottomSheet = view.findViewById(R.id.filterBottomSheet)
        filterBottomSheetBehavior = BottomSheetBehavior.from(filterBottomSheet)

        mSettings = requireActivity().getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)

        filterAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_list_item,
            R.id.filterItem,
            resources.getStringArray(R.array.movie_filter_options).toCollection(mutableListOf())
        )
        binding.filterBottomSheet.optionList.adapter = filterAdapter
        addListListener(filterAdapter)
    }

    private fun setupRecyclerView() {
        listLayoutManager = GridLayoutManager(context, 1)

        profileMoviesAdapter =
            ProfileMoviesAdapter(listLayoutManager, object : OnFavoriteActorClickListener {
                override fun onItemCLick(actor: FavoriteActor) {
                    ProfileFragmentDirections.openActorFromProfile(actor.actorId).also {
                        requireView().findNavController().navigate(it)
                    }
                }
            }, object : OnSimpleMovieClickListener {
                override fun onItemCLick(movie: SimpleMovie) {
                    Toast.makeText(context, "In development", Toast.LENGTH_SHORT).show()
                }
            })

        binding.moviesList.apply {
            layoutManager = listLayoutManager
            adapter = profileMoviesAdapter
        }
    }

    private fun setupTouchHelper() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition.apply {
                    profileMoviesAdapter.deleteItem(this)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                ).addBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.deleteBackground
                    )
                ).addSwipeLeftLabel(getString(R.string.delete))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
                    .create()
                    .decorate()
            }
        }).attachToRecyclerView(binding.moviesList)
    }

    private fun setupListeners() {
        binding.btnChangeRecyclerViewLayout.setOnClickListener {
            listLayoutManager?.let { manager ->
                when (manager.spanCount) {
                    1 -> {
                        manager.spanCount = 2
                        (it as ImageView).setImageResource(R.drawable.ic_list_medium)
                    }
                    2 -> {
                        manager.spanCount = 3
                        (it as ImageView).setImageResource(R.drawable.ic_list_big)
                    }
                    else -> {
                        manager.spanCount = 1
                        (it as ImageView).setImageResource(R.drawable.ic_list)
                    }
                }
                profileMoviesAdapter
                    .notifyItemRangeChanged(0, profileMoviesAdapter.itemCount)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        selectItems(convertWantToMovie(wantMovies))
                        binding.isActorListOpen = false
                        binding.tvDataType.text = getString(R.string.movies)
                    }
                    1 -> {
                        selectItems(convertWatchedToMovie(watchedMovies))
                        binding.isActorListOpen = false
                        binding.tvDataType.text = getString(R.string.movies)
                    }
                    2 -> {
                        listLayoutManager?.spanCount = 1
                        selectItems(favoriteActors)
                        binding.isActorListOpen = true
                        binding.tvDataType.text = getString(R.string.persons)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        binding.btnRevertList.setOnClickListener {
            profileMoviesAdapter.apply {
                revertList()
                when {
                    isReverted() -> (it as ImageView).setImageResource(R.drawable.ic_arrows_up)
                    else -> (it as ImageView).setImageResource(R.drawable.ic_arrows_down)
                }
            }
        }

        binding.btnFilter.setOnClickListener {
            if (filterBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                enableFilerBottomSheet()
            else
                filterBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.filterBottomSheet.btnSaveFilter.setOnClickListener {
            filterBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.btnProfileImage.setOnClickListener {
            requireView().findNavController().navigate(R.id.openSettings)
        }
    }

    private fun startObserve() {
        profileViewModel.readWantMovies.observe(viewLifecycleOwner, {
            wantMovies = it
            applyItems(wantMovies)
        })

        profileViewModel.readWatchMovies.observe(viewLifecycleOwner, {
            watchedMovies = it
            applyItems(watchedMovies)
        })

        profileViewModel.readFavoriteActors.observe(viewLifecycleOwner, {
            favoriteActors = it
            applyItems(favoriteActors)
        })

        profileViewModel.readUserPhoto.observe(viewLifecycleOwner) {
            if (it != null) binding.btnProfileImage.setImageBitmap(it)
            else binding.btnProfileImage.setImageResource(R.drawable.ic_profile)
        }
    }

    private fun selectItems(itemsList: List<Any>) {
        profileMoviesAdapter.apply {
            clearMovies()
            addItems(items = itemsList)
            if (itemsList.isNotEmpty() && itemsList[0] is SimpleMovie) {
                selectFilterOption(getSavedFilter())
            }
            notifyDataSetChanged()
        }
        binding.tvMoviesCount.text = itemsList.count().toString()
    }

    @Suppress("UNCHECKED_CAST")
    private fun applyItems(itemsList: List<Any>) {
        if (itemsList.isEmpty()) return
        if (binding.tabLayout.selectedTabPosition == 0 && itemsList.first() is WantMovie) {
            selectItems(itemsList = convertWantToMovie(itemsList as List<WantMovie>))
        } else if (binding.tabLayout.selectedTabPosition == 1 && itemsList.first() is WatchedMovie) {
            selectItems(itemsList = convertWatchedToMovie(itemsList as List<WatchedMovie>))
        } else if (binding.tabLayout.selectedTabPosition == 2 && itemsList.first() is FavoriteActor) {
            selectItems(itemsList = itemsList)
        }
    }

    private fun setBackButtonBehavior(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (filterBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        filterBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        this.requestFocus()
                        return@OnKeyListener true
                    }
                }
                return@OnKeyListener false
            })
        }
    }

    private fun enableFilerBottomSheet() {
        when (binding.tabLayout.selectedTabPosition) {
            0 -> binding.filterBottomSheet.tvTitle.text = getString(R.string.want_title)
            1 -> binding.filterBottomSheet.tvTitle.text = getString(R.string.watched_title)
            2 -> binding.filterBottomSheet.tvTitle.text = getString(R.string.actors_title)
        }
        filterBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun addListListener(arrayAdapter: ArrayAdapter<String>) {
        binding.filterBottomSheet.optionList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                run {
                    selectFilterOption(option = position)
                    saveFilterSetting(settingId = position)
                    unselectOptions(option = position, parent = parent)
                    arrayAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun unselectOptions(
        option: Int,
        parent: AdapterView<*>,
    ) {
        for (i in 0 until parent.count) {
            if (i == option) continue
            parent.getChildAt(i).apply {
                findViewById<TextView>(R.id.filterItem).setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.lightGray
                    )
                )
                findViewById<CheckBox>(R.id.checkBox).isChecked = false
            }
        }
    }

    private fun saveFilterSetting(settingId: Int) {
        mSettings.edit().apply {
            putInt(
                APP_PREFERENCE_FILTERING_OPTION,
                settingId
            )
            apply()
        }
    }

    private fun getSavedFilter() =
        mSettings.getInt(APP_PREFERENCE_FILTERING_OPTION, STANDARD_FILTER_OPTION)

    private fun selectFilterOption(option: Int) {
        profileMoviesAdapter.apply {
            when (option) {
                0 -> alphabetFiler()
                1 -> dateFilter()
                2 -> releaseFilter()
                3 -> ratingFilter()
            }
        }
    }
}