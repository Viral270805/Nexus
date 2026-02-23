package com.example.nexus.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexus.DashboardActivity
import com.example.nexus.databinding.ActivityLoginBinding
import com.example.nexus.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    // This line has been corrected
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (!validateInput(email, password)) {
            return
        }

        // --- DEMO LOGIC ---
        // In a real app, you would validate this against Firebase Auth or your backend.
        // We will use the demo account: demo@nexus.com / 12345
        if (email == "demo@nexus.com" && password == "12345") {
            // Save the session
            sessionManager.saveSession("Demo User", email)

            // Navigate to Dashboard
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
        }
        // --- END DEMO LOGIC ---
    }

    private fun validateInput(email: String, pass: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email cannot be empty"
            binding.etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }

        if (pass.isEmpty()) {
            binding.etPassword.error = "Password cannot be empty"
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }
}