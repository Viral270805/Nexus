package com.example.nexus

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nexus.auth.LoginActivity
import com.example.nexus.databinding.ActivityDashboardBinding
import com.example.nexus.utils.SessionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sessionManager: SessionManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                fetchLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                fetchLocation()
            }
            else -> {
                // No location access granted.
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                updateNavHeaderLocation("Location Denied")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupNavigation()
        updateNavHeader()
        requestLocationPermission()
    }

    private fun setupNavigation() {
        // Set the toolbar as the action bar
        setSupportActionBar(binding.toolbar)

        // Find the NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        // Define which destinations are "top-level" (don't show an "Up" arrow)
        // These IDs must match your res/navigation/nav_graph.xml
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard_fragment, R.id.nav_profile, R.id.nav_settings
            ), binding.drawerLayout // Pass the DrawerLayout
        )

        // Connect the NavController to the ActionBar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Connect the NavigationView to the NavController
        binding.navView.setupWithNavController(navController)

        // Handle the "Logout" button click from the nav menu
        binding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            sessionManager.clearSession()
            val intent = Intent(this, LoginActivity::class.java)
            // Clear the activity stack so the user can't press "Back" to go to the dashboard
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            true
        }
    }

    /**
     * Updates the Navigation Drawer's header with the user's name from the session.
     */
    private fun updateNavHeader() {
        val headerView = binding.navView.getHeaderView(0) // Get the header
        val tvUserName: TextView = headerView.findViewById(R.id.tvUserName)
        // Set user's name
        tvUserName.text = sessionManager.getUserName()
    }

    /**
     * Checks for location permissions. If granted, fetches location.
     * If not, launches the permission request.
     */
    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Launch the permission request
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            // Permissions are already granted
            fetchLocation()
        }
    }

    /**
     * Fetches the user's last known location and updates the nav header.
     * This is marked @SuppressLint because we are checking permission in requestLocationPermission()
     */
    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    // Use Geocoder to get city name from lat/long
                    val geocoder = Geocoder(this, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        // Get the city name
                        val cityName = addresses?.firstOrNull()?.locality ?: "Unknown City"
                        updateNavHeaderLocation(cityName)
                    } catch (e: Exception) {
                        Log.e("DashboardActivity", "Geocoder error", e)
                        updateNavHeaderLocation("Unknown Location")
                    }
                } else {
                    updateNavHeaderLocation("Location not found")
                }
            }
            .addOnFailureListener {
                updateNavHeaderLocation("Location error")
            }
    }

    /**
     * Updates the location TextView in the Navigation Drawer's header.
     */
    private fun updateNavHeaderLocation(location: String) {
        val headerView = binding.navView.getHeaderView(0)
        val tvLocation: TextView = headerView.findViewById(R.id.tvLocation)
        tvLocation.text = location
    }

    /**
     * This is required to make the "Up" button (<-) in the toolbar
     * work correctly with the Navigation Drawer and NavController.
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * This handles the "Back" button press. If the Navigation Drawer is open,
     * it closes the drawer. Otherwise, it performs the default back action.
     */
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}