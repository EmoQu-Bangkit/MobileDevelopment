package com.capstone.emoqu.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.ui.add.AddFragment
import com.capstone.emoqu.ui.report.ReportFragment
import com.capstone.emoqu.ui.today.TodayFragment
import com.capstone.emoqu.utils.SyncDataWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var addFab: FloatingActionButton
    private lateinit var navCard: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityRoomDatabase.getInstance(this)
        scheduleSync()

        bottomNav = findViewById(R.id.bottom_nav)
        addFab = findViewById(R.id.fab_add)
        navCard = findViewById(R.id.nav_card)

//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_profile -> {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectedFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncDataWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncDataWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

}
