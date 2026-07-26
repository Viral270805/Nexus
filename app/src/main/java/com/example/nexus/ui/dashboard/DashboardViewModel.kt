package com.example.nexus.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.FirebaseRepository
import com.example.nexus.network.PatientStatusFirebase
import com.example.nexus.network.SystemStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()
    
    private val _systemStatus = MutableLiveData<SystemStatus?>()
    val systemStatus: LiveData<SystemStatus?> = _systemStatus

    private val _alertCount = MutableLiveData<Int>()
    val alertCount: LiveData<Int> = _alertCount

    fun startListening() {
        // Listen to patient status from Firebase
        viewModelScope.launch {
            firebaseRepository.getPatientStatus().collectLatest { fbStatus ->
                // Map Firebase model to existing SystemStatus for UI compatibility
                _systemStatus.postValue(SystemStatus(
                    status = if (fbStatus.online) "online" else "offline",
                    pir_active = fbStatus.motion_detected,
                    camera_active = fbStatus.camera_active,
                    mic_listening = fbStatus.mic_active
                ))
            }
        }

        // Listen to alerts for the count
        viewModelScope.launch {
            firebaseRepository.getAlerts().collectLatest { alerts ->
                _alertCount.postValue(alerts.size)
            }
        }
    }

    fun startPolling() {
        startListening()
    }

    fun stopPolling() {
        // Handled by viewModelScope
    }
}