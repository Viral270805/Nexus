package com.example.nexus.ui.features

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.RetrofitClient
import kotlinx.coroutines.launch

class LiveCameraViewModel(application: Application) : AndroidViewModel(application) {

    private val _motionStatus = MutableLiveData<Boolean>()
    val motionStatus: LiveData<Boolean> = _motionStatus

    private val _gestureStatus = MutableLiveData<Boolean>()
    val gestureStatus: LiveData<Boolean> = _gestureStatus

    private val _lastMotion = MutableLiveData<String>()
    val lastMotion: LiveData<String> = _lastMotion

    fun fetchStatus() {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient(getApplication())
                val motion = api.getMotionStatus()
                _motionStatus.postValue(motion["active"] as? Boolean ?: false)
                _lastMotion.postValue(motion["last_motion"] as? String ?: "--:--")

                val gesture = api.getGestureStatus()
                _gestureStatus.postValue(gesture["enabled"] as? Boolean ?: false)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun startDetection() {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).startMotionDetection()
                fetchStatus()
            } catch (e: Exception) {}
        }
    }

    fun captureFrame() {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).captureFrame()
            } catch (e: Exception) {}
        }
    }

    fun setMotionSensitivity(value: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).setMotionSensitivity(mapOf("value" to value))
            } catch (e: Exception) {}
        }
    }

    fun toggleGesture(enabled: Boolean) {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).toggleGesture(mapOf("enabled" to enabled))
                fetchStatus()
            } catch (e: Exception) {}
        }
    }
}