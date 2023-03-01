package com.kemsky.pokedex.presentation.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kemsky.pokedex.R
import com.kemsky.pokedex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        configureBottomNavigationBar()
    }

    private fun configureBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding?.bottomNavigatinView?.setupWithNavController(navController)
        binding?.bottomNavigatinView?.itemActiveIndicatorColor = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.black))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}