package com.capstone.emoqu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.capstone.emoqu.R
import com.capstone.emoqu.ui.add.AddFragment
import com.capstone.emoqu.ui.report.ReportFragment
import com.capstone.emoqu.ui.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var addFab: FloatingActionButton
    private lateinit var navCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)
        addFab = findViewById(R.id.fab_add)
        navCard = findViewById(R.id.nav_card)


        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_today -> {
                    selectedFragment(TodayFragment())
                    title = "Today"
                }

                R.id.bottom_report -> {
                    selectedFragment(ReportFragment())
                    title = "Report"
                }
            }
            true
        }

        selectedFragment(TodayFragment())
        title = "Today"
        bottomNav.selectedItemId = R.id.bottom_today

        addFab.setOnClickListener {
            selectedFragment(AddFragment())
            title = "Add"

            addFab.visibility = View.GONE
            navCard.visibility = View.GONE
        }
    }

    private fun selectedFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
