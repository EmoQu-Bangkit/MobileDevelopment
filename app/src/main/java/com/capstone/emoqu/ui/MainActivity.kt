package com.capstone.emoqu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.emoqu.R
import com.capstone.emoqu.ui.fragment.AddFragment
import com.capstone.emoqu.ui.fragment.ReportFragment
import com.capstone.emoqu.ui.fragment.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.bottom_today -> {
                    selectedFragment = TodayFragment()
                }
                R.id.fab_add -> {
                    // Handle the floating action button click here if necessary
                    // Since it's disabled, this block may not be needed
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.bottom_report -> {
                    selectedFragment = ReportFragment()
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
            true
        }

        // Set the default selected item
        bottomNav.selectedItemId = R.id.bottom_today
    }
}