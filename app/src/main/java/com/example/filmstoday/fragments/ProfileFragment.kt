package com.example.filmstoday.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ProfileMoviesAdapter
import com.example.filmstoday.data.FavoriteActor
import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie
import com.example.filmstoday.databinding.FragmentProfileBinding
import com.example.filmstoday.utils.*
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE_ADULT_CONTENT
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE_FILTERING_OPTION
import com.example.filmstoday.utils.Constants.Companion.STANDARD_FILTER_OPTION
import com.example.filmstoday.viewmodels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private var listLayoutManager: GridLayoutManager? = null
    private lateinit var wantMovies: List<WantMovie>
    private lateinit var watchedMovies: List<WatchedMovie>
    private lateinit var favoriteActors: List<FavoriteActor>

    private lateinit var profileMoviesAdapter: ProfileMoviesAdapter
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileBottomSheet: View
    private lateinit var filterBottomSheet: View
    private lateinit var filterBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var profileBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var mSettings: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(profileViewModel)
        doInitialization(view)
        setupBottomSheets()
        setupRecyclerView()
        setupTouchHelper()
        setupListeners()
        startObserve()
    }

    private fun doInitialization(view: View) {
        profileBottomSheet = view.findViewById(R.id.profileBottomSheet)
        profileBottomSheetBehavior = BottomSheetBehavior.from(profileBottomSheet)

        filterBottomSheet = view.findViewById(R.id.filterBottomSheet)
        filterBottomSheetBehavior = BottomSheetBehavior.from(filterBottomSheet)

        mSettings = requireActivity().getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)

        binding.profileBottomSheet.switchAdultContent.apply {
            if (mSettings.getBoolean(APP_PREFERENCE_ADULT_CONTENT, true)) isChecked = true
        }

        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.filter_list_item,
            R.id.filterItem,
            resources.getStringArray(R.array.Filter_Options)
        )
        binding.filterBottomSheet.optionList.adapter = arrayAdapter
        addListListener(arrayAdapter)
    }

    private fun setupBottomSheets() {
        binding.btnProfileImage.setOnClickListener {
            profileBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.profileBottomSheet.btnSaveSettings.setOnClickListener {
            saveAdultContentSetting()
            profileBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            Toast.makeText(context, "Settings were saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        listLayoutManager = GridLayoutManager(context, 1)
        profileMoviesAdapter = ProfileMoviesAdapter(listLayoutManager)
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
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewHolder.adapterPosition.apply {
//                    profileViewModel.deleteMovie(profileMoviesAdapter.getItemItemAt(this))
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
                isCurrentlyActive: Boolean
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
                        binding.btnChangeRecyclerViewLayout.show()
                        binding.tvDataType.text = getString(R.string.movies)
                    }
                    1 -> {
                        selectItems(convertWatchedToMovie(watchedMovies))
                        binding.btnChangeRecyclerViewLayout.show()
                        binding.tvDataType.text = getString(R.string.movies)
                    }
                    2 -> {
                        listLayoutManager?.spanCount = 1
                        selectItems(favoriteActors)
                        binding.btnChangeRecyclerViewLayout.hide()
                        binding.tvDataType.text = getString(R.string.persons)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding.btnRevertList.setOnClickListener {
            profileMoviesAdapter.apply {
                revertList()
                when {
                    isReverted() -> (it as ImageView).setImageResource(R.drawable.ic_arrow_up)
                    else -> (it as ImageView).setImageResource(R.drawable.ic_arrow_down)
                }
            }
        }

        binding.btnFilter.setOnClickListener {
            enableFilerBottomSheet()
        }

        binding.filterBottomSheet.btnSaveFilter.setOnClickListener {
            filterBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun startObserve() {
        profileViewModel.readWantMovies.observe(viewLifecycleOwner, {
            wantMovies = it
            selectItems(convertWantToMovie(it))
        })

        profileViewModel.readWatchMovies.observe(viewLifecycleOwner, {
            watchedMovies = it
        })

        profileViewModel.readFavoriteActors.observe(viewLifecycleOwner, {
            favoriteActors = it
        })
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
            AdapterView.OnItemClickListener { parent, view, position, _ ->
                run {
                    selectFilterOption(option = position)
                    saveFilterSetting(position)
                    unselectOptions(option = position, parent = parent)
                    arrayAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun unselectOptions(
        option: Int,
        parent: AdapterView<*>
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

    private fun selectItems(itemsList: List<Any>) {
        profileMoviesAdapter.apply {
            clearMovies()
            addItems(items = itemsList)
            selectFilterOption(getSavedFilter())
            notifyDataSetChanged()
        }
        binding.tvMoviesCount.text = itemsList.count().toString()
    }

    private fun saveAdultContentSetting() {
        mSettings.edit().apply {
            putBoolean(
                APP_PREFERENCE_ADULT_CONTENT,
                binding.profileBottomSheet.switchAdultContent.isChecked
            )
            apply()
        }
    }

    private fun saveFilterSetting(ordinal: Int) {
        mSettings.edit().apply {
            putInt(
                APP_PREFERENCE_FILTERING_OPTION,
                ordinal
            )
            apply()
        }
    }

    private fun getSavedFilter() =
        mSettings.getInt(APP_PREFERENCE_FILTERING_OPTION, STANDARD_FILTER_OPTION)

    private fun selectFilterOption(option: Int) {
        when (option) {
            0 -> profileMoviesAdapter.alphabetFiler()
            1 -> profileMoviesAdapter.releaseFilter()
            2 -> profileMoviesAdapter.ratingFilter()
            3 -> profileMoviesAdapter.dateFilter()
        }

        binding.filterBottomSheet.optionList.getChildAt(option).apply {
            findViewById<CheckBox>(R.id.checkBox).isChecked = true
            findViewById<TextView>(R.id.filterItem)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }
}