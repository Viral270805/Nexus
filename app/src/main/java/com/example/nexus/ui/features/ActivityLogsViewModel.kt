package com.example.nexus.ui.features

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.FirebaseRepository
import com.example.nexus.network.LogEntry
import com.example.nexus.network.RetrofitClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ActivityLogsViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()

    private val _logs = MutableLiveData<List<LogEntry>>()
    val logs: LiveData<List<LogEntry>> = _logs

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun startListening() {
        _isLoading.value = true
        viewModelScope.launch {
            firebaseRepository.getLogs().collectLatest { logsList ->
                _logs.postValue(logsList)
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

    fun clearLogs() {
        viewModelScope.launch {
            try {
                // We still call hardware to clear logs, then Firestore will sync the empty state
                RetrofitClient.getClient(getApplication()).clearLogs()
            } catch (e: Exception) {
            }
        }
    }
}