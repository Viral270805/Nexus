package com.example.nexus.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("NexusAppPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_NAME = "user_name"
        private const val USER_EMAIL = "user_email"
        private const val RPI_IP = "rpi_ip"
        private const val DARK_MODE = "dark_mode"
        private const val MOTION_ALERTS = "motion_alerts"
        private const val GESTURE_MODE = "gesture_mode"
        private const val BUZZER_ALERTS = "buzzer_alerts"
        private const val SPEAKER_ENABLED = "speaker_enabled"
    }

    fun saveSession(name: String, email: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, "dummy_token_12345")
        editor.putString(USER_NAME, name)
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getString(USER_TOKEN, null) != null
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun getUserName(): String {
        return prefs.getString(USER_NAME, "Nexus User") ?: "Nexus User"
    }

    fun getUserEmail(): String {
        return prefs.getString(USER_EMAIL, "user@nexus.com") ?: "user@nexus.com"
    }

    fun saveRPiIP(ip: String) {
        prefs.edit().putString(RPI_IP, ip).apply()
    }

    fun getRPiIP(): String? {
        return prefs.getString(RPI_IP, "10.51.173.82")
    }

    // Toggle Persistence
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(DARK_MODE, enabled).apply()
    }
    fun isDarkMode(): Boolean = prefs.getBoolean(DARK_MODE, true)

    fun setMotionAlerts(enabled: Boolean) {
        prefs.edit().putBoolean(MOTION_ALERTS, enabled).apply()
    }
    fun areMotionAlertsEnabled(): Boolean = prefs.getBoolean(MOTION_ALERTS, true)

    fun setGestureMode(enabled: Boolean) {
        prefs.edit().putBoolean(GESTURE_MODE, enabled).apply()
    }
    fun isGestureModeEnabled(): Boolean = prefs.getBoolean(GESTURE_MODE, false)

    fun setBuzzerAlerts(enabled: Boolean) {
        prefs.edit().putBoolean(BUZZER_ALERTS, enabled).apply()
    }
    fun areBuzzerAlertsEnabled(): Boolean = prefs.getBoolean(BUZZER_ALERTS, true)

    fun setSpeakerEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(SPEAKER_ENABLED, enabled).apply()
    }
    fun isSpeakerEnabled(): Boolean = prefs.getBoolean(SPEAKER_ENABLED, true)
}