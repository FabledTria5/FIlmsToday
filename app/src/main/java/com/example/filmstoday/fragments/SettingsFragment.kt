package com.example.filmstoday.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmstoday.R
import com.example.filmstoday.data.User
import com.example.filmstoday.databinding.FragmentSettingsBinding
import com.example.filmstoday.utils.Constants
import com.example.filmstoday.utils.Constants.Companion.APP_PREFERENCE_ADULT_CONTENT
import com.example.filmstoday.viewmodels.SettingsViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mSettings: SharedPreferences

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doInitialization()
        checkAdultContent()
        setupUserSettings()
        addListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                binding.rivUserImage.setImageURI(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                throw result.error
            }
        }
    }

    private fun addListeners() {
        binding.btnSaveSettings.setOnClickListener {
            saveSettings()
        }

        binding.btnChangeUserName.setOnClickListener {
            binding.tvUserName.apply {
                isEnabled = true
                requestFocus()
                if (binding.tvUserName.text.toString() == getString(R.string.default_user_name)) {
                    binding.tvUserName.text.clear()
                }
            }
        }

        binding.rivUserImage.setOnClickListener {
            CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(requireContext(), this)
        }
    }

    private fun doInitialization() {
        mSettings =
            requireActivity().getSharedPreferences(Constants.APP_PREFERENCE, Context.MODE_PRIVATE)
    }

    private fun setupUserSettings() {
        settingsViewModel.getUser().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvUserName.setText(it.userName)
                binding.rivUserImage.setImageBitmap(it.userPhoto)
            } else {
                binding.tvUserName.setText(getString(R.string.default_user_name))
                binding.rivUserImage.setImageResource(R.drawable.ic_profile)
            }
            binding.tvUserName.isEnabled = false
        }
    }

    private fun checkAdultContent() = binding.apply {
        isAdultContentAvailable = mSettings.getBoolean(APP_PREFERENCE_ADULT_CONTENT, true)
    }

    private fun saveSettings() {
        mSettings.edit().apply {
            putBoolean(APP_PREFERENCE_ADULT_CONTENT, binding.switchAdultContent.isChecked)
            apply()
        }
        settingsViewModel.saveUserData(
            User(
                0,
                binding.tvUserName.text.toString(),
                (binding.rivUserImage.drawable as BitmapDrawable).bitmap
            )
        )
        findNavController().navigateUp()
    }
}

