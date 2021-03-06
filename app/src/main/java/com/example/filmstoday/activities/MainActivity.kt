package com.example.filmstoday.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.filmstoday.R
import com.example.filmstoday.databinding.MainActivityBinding
import com.example.filmstoday.receivers.NetworkReceiver
import com.example.filmstoday.repositories.MainActivityRepository
import com.example.filmstoday.utils.toast
import com.example.filmstoday.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        networkReceiver = NetworkReceiver()
        setContentView(binding.root)
        setupNavigation()
        setupConnectionListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movies_nav, R.id.search_nav, R.id.profile_nav ->
                    binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setupConnectionListener() {
        val filterNetwork = IntentFilter()
        MainActivityRepository.instance().addDataSource(networkReceiver.getData())
        filterNetwork.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filterNetwork)

        mainActivityViewModel.getData().observe(this, {
            if (it) toast(message = getString(R.string.connection_lost))
        })
    }
}