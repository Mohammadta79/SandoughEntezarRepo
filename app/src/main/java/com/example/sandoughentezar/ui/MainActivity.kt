package com.example.sandoughentezar.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        selectedViews()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_view_fragmentContainer) as NavHostFragment?
        val navController = navHostFragment!!.navController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            binding.drawerLayout.closeDrawer(Gravity.RIGHT)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.imgOpenDrawer.id -> {
                binding.drawerLayout.openDrawer(Gravity.RIGHT)
            }
        }
    }

    private fun selectedViews() {
        binding.imgOpenDrawer.setOnClickListener(this)
    }

}