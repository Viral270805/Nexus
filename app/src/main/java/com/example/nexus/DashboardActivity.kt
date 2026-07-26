package com.example.nexus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nexus.auth.LoginActivity
import com.example.nexus.databinding.ActivityDashboardBinding
import com.example.nexus.network.FirebaseRepository
import com.example.nexus.utils.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sessionManager: SessionManager
    private val firebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setupNavigation()
        observeSystemStatus()
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard_fragment, R.id.nav_voice, R.id.nav_camera, 
                R.id.nav_alerts, R.id.nav_logs, R.id.nav_settings
            ), binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            sessionManager.clearSession()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            true
        }
    }

    private fun observeSystemStatus() {
        lifecycleScope.launch {
            firebaseRepository.getPatientStatus().collectLatest { status ->
                updateNavHeader(status.online)
            }
        }
    }

    private fun updateNavHeader(isOnline: Boolean) {
        val headerView = binding.navView.getHeaderView(0)
        val dot = headerView.findViewById<View>(R.id.dotNavStatus)
        val text = headerView.findViewById<TextView>(R.id.tvNavStatus)

        if (isOnline) {
            dot?.setBackgroundResource(R.drawable.status_dot_green)
            text?.text = "System Online"
            text?.setTextColor(ContextCompat.getColor(this, R.color.green_success))
        } else {
            dot?.setBackgroundResource(R.drawable.status_dot_grey)
            text?.text = "System Offline"
            text?.setTextColor(ContextCompat.getColor(this, R.color.secondary_text))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}