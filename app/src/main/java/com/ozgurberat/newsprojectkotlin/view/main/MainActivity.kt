package com.ozgurberat.newsprojectkotlin.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()

    }

    private fun setUpNavigation() {
        val navController = Navigation.findNavController(this, R.id.main_fragment_container)
        main_bottom_nav.setOnNavigationItemSelectedListener {
            if (it.itemId != main_bottom_nav.selectedItemId) {
                NavigationUI.onNavDestinationSelected(it, navController)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {

    }
}