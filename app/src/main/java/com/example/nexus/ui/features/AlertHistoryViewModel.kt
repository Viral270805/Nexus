package com.example.nexus.ui.features

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.Alert
import com.example.nexus.network.AlertSummary
import com.example.nexus.network.FirebaseRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()

    private val _alerts = MutableLiveData<List<Alert>>()
    val alerts: LiveData<List<Alert>> = _alerts

    private val _summary = MutableLiveData<AlertSummary>()
    val summary: LiveData<AlertSummary> = _summary

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    fun startListening() {
        _isLoading.value = true
        viewModelScope.launch {
            firebaseRepository.getAlerts().collectLatest { alertList ->
                _alerts.postValue(alertList)
                
                // Calculate summary from the list since we have real-time updates
                val motion = alertList.count { it.type.lowercase().contains("motion") }
                val gesture = alertList.count { it.type.lowercase().contains("gesture") }
                val voice = alertList.count { it.type.lowercase().contains("voice") }
                _summary.postValue(AlertSummary(motion, gesture, voice))
                
                _isConnected.postValue(true)
                _isLoading.postValue(false)
            }
        }
    }

    // Compatibility method for existing fragments calling startPolling
    fun startPolling() {
        startListening()
    }

    fun stopPolling() {
        // Firebase listeners are handled by Flow/viewModelScope
    }
}