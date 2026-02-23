package com.example.nexus.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nexus.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendOtp.setOnClickListener {
            handleSendOtp()
        }

        binding.btnResetPassword.setOnClickListener {
            handleResetPassword()
        }
    }

    private fun handleSendOtp() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Please enter a valid email"
            binding.etEmail.requestFocus()
            return
        }

        // --- DEMO LOGIC ---
        // Simulate sending an OTP. In a real app, you'd call Firebase or your backend.
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSendOtp.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.btnSendOtp.isEnabled = true

            // Show a dummy OTP
            val dummyOtp = (100000..999999).random()
            Toast.makeText(this, "OTP Sent (Demo): $dummyOtp", Toast.LENGTH_LONG).show()

            // Show the next steps
            binding.tilEmail.visibility = View.GONE
            binding.btnSendOtp.visibility = View.GONE
            binding.tilOtp.visibility = View.VISIBLE
            binding.tilNewPassword.visibility = View.VISIBLE
            binding.btnResetPassword.visibility = View.VISIBLE
            binding.tvTitle.text = "Verify OTP"

        }, 2000)
        // --- END DEMO LOGIC ---
    }

    private fun handleResetPassword() {
        val otp = binding.etOtp.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()

        if (otp.length != 6) {
            binding.etOtp.error = "OTP must be 6 digits"
            binding.etOtp.requestFocus()
            return
        }

        if (newPassword.length < 6) {
            binding.etNewPassword.error = "Password must be at least 6 characters"
            binding.etNewPassword.requestFocus()
            return
        }

        // --- DEMO LOGIC ---
        // Simulate success and return to Login
        Toast.makeText(this, "Password reset successfully! Please login.", Toast.LENGTH_LONG).show()
        finish()
        // --- END DEMO LOGIC ---
    }
}