package com.example.filmstoday.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.filmstoday.R
import com.example.filmstoday.viewmodels.MoviesViewModel
import com.example.filmstoday.databinding.FragmentMoviesBinding
import com.google.android.material.tabs.TabLayout

class MoviesFragment : Fragment() {

    private val LOG_TAG = "MoviesFragment"

    private lateinit var viewModel: MoviesViewModel
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)

        val tabLayoutTab : TabLayout.Tab? = binding.tabLayout.getTabAt(viewModel.selectedPosition)
        tabLayoutTab?.select()


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) = viewModel.selectItem(tab?.position)

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}