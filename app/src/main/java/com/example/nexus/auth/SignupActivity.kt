package com.example.nexus.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexus.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            handleSignup()
        }

        binding.tvLogin.setOnClickListener {
            // Go back to Login
            finish()
        }
    }

    private fun handleSignup() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (!validateInput(name, email, password, confirmPassword)) {
            return
        }

        // --- DEMO LOGIC ---
        // In a real app, you would save this to Firebase Auth or your backend.
        // For this demo, we'll just show a success message and go to Login.
        Toast.makeText(this, "Signup Successful! Please login.", Toast.LENGTH_LONG).show()

        // Redirect to Login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        // --- END DEMO LOGIC ---
    }

    private fun validateInput(name: String, email: String, pass: String, confirmPass: String): Boolean {
        if (name.isEmpty()) {
            binding.etName.error = "Name cannot be empty"
            binding.etName.requestFocus()
            return false
        }

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

        if (pass.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            binding.etPassword.requestFocus()
            return false
        }

        if (confirmPass != pass) {
            binding.etConfirmPassword.error = "Passwords do not match"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }
}