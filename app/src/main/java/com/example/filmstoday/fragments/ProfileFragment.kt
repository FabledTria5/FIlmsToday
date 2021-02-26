package com.example.filmstoday.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.filmstoday.R
import com.example.filmstoday.databinding.FragmentProfileBinding
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE_ADULT_CONTENT
import com.example.filmstoday.viewmodels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

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