package com.example.filmstoday.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.filmstoday.R
import com.example.filmstoday.databinding.MainActivityBinding
import com.example.filmstoday.receivers.NetworkReceiver
import com.example.filmstoday.repositories.MainActivityRepository
import com.example.filmstoday.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private lateinit var networkReceiver: NetworkReceiver
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        networkReceiver = NetworkReceiver()
        setContentView(binding.root)
        setupNavigation()
        setupConnectionListener()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupConnectionListener() {
        val filterNetwork = IntentFilter()
        MainActivityRepository.instance().addDataSource(networkReceiver.getData())
        filterNetwork.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filterNetwork)

        mainActivityViewModel.getData().observe(this, {
            if (it) showMessage(getString(R.string.connection_lost))
        })
    }

    private fun showMessage(message: String) {
        val toast =
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 60)
        toast.show()
    }
}