package com.example.nexus.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages the user's session data (login state, name, email) using SharedPreferences.
 */
class SessionManager(context: Context) {

    // Private preferences file
    private val prefs: SharedPreferences =
        context.getSharedPreferences("NexusAppPrefs", Context.MODE_PRIVATE)

    companion object {
        // Keys for storing data
        private const val USER_TOKEN = "user_token"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
    }

    /**
     * Saves user session data upon successful login.
     * @param name The user's display name.
     * @param email The user's email.
     */
    fun saveSession(name: String, email: String) {
        val editor = prefs.edit()
        // In a real app, you'd save a real token from Firebase/your backend
        editor.putString(USER_TOKEN, "dummy_token_12345")
        editor.putString(USER_NAME, name)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    /**
     * Checks if a user is currently logged in (i.e., has a token).
     * @return True if logged in, false otherwise.
     */
    fun isLoggedIn(): Boolean {
        // We check if the token is not null
        return prefs.getString(USER_TOKEN, null) != null
    }

    /**
     * Clears all saved session data upon logout.
     */
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Fetches the logged-in user's name.
     * @return The saved name, or a default value.
     */
    fun getUserName(): String {
        return prefs.getString(USER_NAME, "Nexus User") ?: "Nexus User"
    }

    /**
     * Fetches the logged-in user's email.
     * @return The saved email, or a default value.
     */
    fun getUserEmail(): String {
        return prefs.getString(USER_EMAIL, "user@nexus.com") ?: "user@nexus.com"
    }
}