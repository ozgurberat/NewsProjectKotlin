package com.ozgurberat.newsprojectkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment : NavHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(main_bottom_nav, navHostFragment.navController)
    }
}