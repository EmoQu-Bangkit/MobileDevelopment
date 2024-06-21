package com.capstone.emoqu.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.ui.add.AddFragment
import com.capstone.emoqu.ui.auth.login.LoginActivity
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.ui.auth.pref.AuthViewModel
import com.capstone.emoqu.ui.auth.pref.dataStore
import com.capstone.emoqu.ui.report.ReportFragment
import com.capstone.emoqu.ui.today.TodayFragment
import com.capstone.emoqu.utils.SyncDataWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val overflowIcon: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_setting)
        toolbar.overflowIcon = overflowIcon


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
            selectedFragment(AddFragment(), true)
            title = "Add"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_logout -> {
                logout(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectedFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }


    fun hideBottomMenu() {
        findViewById<View>(R.id.fab_add).visibility = View.GONE
        findViewById<View>(R.id.nav_card).visibility = View.GONE
    }

    fun showBottomMenu() {
        findViewById<View>(R.id.fab_add).visibility = View.VISIBLE
        findViewById<View>(R.id.nav_card).visibility = View.VISIBLE
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

    private fun logout(context: Context) {
        val authPreferences = AuthPreferences.getInstance(context.dataStore)

        // Memanggil clearSession di dalam blok coroutine
        GlobalScope.launch(Dispatchers.Main) {
            authPreferences.clearSession()

            Toast.makeText(context, R.string.success_logout, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
        }
    }
}
