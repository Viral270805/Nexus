package com.example.nexus.network

data class PatientStatusFirebase(
    val online: Boolean = false,
    val camera_active: Boolean = false,
    val mic_active: Boolean = false,
    val gesture_active: Boolean = false,
    val motion_detected: Boolean = false
)