package com.example.filmstoday.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmstoday.R
import com.example.filmstoday.adapters.ProfileMoviesAdapter
import com.example.filmstoday.data.WantMovie
import com.example.filmstoday.data.WatchedMovie
import com.example.filmstoday.databinding.FragmentProfileBinding
import com.example.filmstoday.models.movie.SimpleMovie
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE_ADULT_CONTENT
import com.example.filmstoday.utils.convertWantToMovie
import com.example.filmstoday.utils.convertWatchedToMovie
import com.example.filmstoday.viewmodels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private var listLayoutManager: GridLayoutManager? = null
    private lateinit var wantMovies: List<WantMovie>
    private lateinit var watchedMovies: List<WatchedMovie>

    private lateinit var profileMoviesAdapter: ProfileMoviesAdapter
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileBottomSheet: View
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
        setupListeners()
        startObserve()
    }

    private fun doInitialization(view: View) {
        profileBottomSheet = view.findViewById(R.id.profileBottomSheet)
        profileBottomSheetBehavior = BottomSheetBehavior.from(profileBottomSheet)
        mSettings = requireActivity().getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)

        binding.profileBottomSheet.switchAdultContent.apply {
            if (mSettings.getBoolean(APP_PREFERENCE_ADULT_CONTENT, true)) isChecked = true
        }
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

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> selectMovies(convertWantToMovie(wantMovies))
                    1 -> selectMovies(convertWatchedToMovie(watchedMovies))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun startObserve() {
        profileViewModel.readWantMovies.observe(viewLifecycleOwner, {
            wantMovies = it
            selectMovies(convertWantToMovie(it))
        })

        profileViewModel.readWatchMovies.observe(viewLifecycleOwner, {
            profileMoviesAdapter.apply {
                watchedMovies = it
            }
        })
    }

    private fun selectMovies(moviesList: List<SimpleMovie>) {
        profileMoviesAdapter.apply {
            clearMovies()
            addMovies(movies = moviesList)
            notifyDataSetChanged()
        }
        binding.tvMoviesCount.text = moviesList.count().toString()
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
}