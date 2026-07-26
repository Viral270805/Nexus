package com.example.nexus.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.RetrofitClient
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _connectionResult = MutableLiveData<String>()
    val connectionResult: LiveData<String> = _connectionResult

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun testConnection() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient(getApplication())
                api.ping()
                _connectionResult.postValue("Connected successfully!")
                _isSuccess.postValue(true)
            } catch (e: Exception) {
                _connectionResult.postValue("Connection failed: ${e.message}")
                _isSuccess.postValue(false)
            }
        }
    }

    fun setMotionSensitivity(value: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).setMotionSensitivity(mapOf("value" to value))
            } catch (e: Exception) {}
        }
    }

    fun toggleMotionAlerts(enabled: Boolean) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).toggleMotionAlerts(mapOf("enabled" to enabled))
            } catch (e: Exception) {}
        }
    }

    fun setGestureSensitivity(value: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).setGestureSensitivity(mapOf("value" to value))
            } catch (e: Exception) {}
        }
    }

    fun toggleGesture(enabled: Boolean) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).toggleGesture(mapOf("enabled" to enabled))
            } catch (e: Exception) {}
        }
    }

    fun setMicGain(value: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).setMicGain(mapOf("value" to value))
            } catch (e: Exception) {}
        }
    }

    fun toggleBuzzer(enabled: Boolean) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).toggleBuzzer(mapOf("enabled" to enabled))
            } catch (e: Exception) {}
        }
    }

    fun toggleSpeaker(enabled: Boolean) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).toggleSpeaker(mapOf("enabled" to enabled))
            } catch (e: Exception) {}
        }
    }
}