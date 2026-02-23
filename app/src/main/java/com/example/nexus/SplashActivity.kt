package com.example.nexus

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.nexus.auth.LoginActivity
import com.example.nexus.databinding.ActivitySplashBinding
import com.example.nexus.utils.SessionManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This line links this Kotlin file to your activity_splash.xml layout
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Add a simple fade-in animation
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        // This line is now fixed. We only animate the text.
        binding.splashText.startAnimation(fadeIn)


        Handler(Looper.getMainLooper()).postDelayed({
            // Check if user is logged in
            if (sessionManager.isLoggedIn()) {
                // Go to Dashboard
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                // Go to Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
            // Finish this activity so user can't go back to it
            finish()
        }, 3000) // 3-second delay
    }
}