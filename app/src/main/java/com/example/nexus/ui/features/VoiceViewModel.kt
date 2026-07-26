package com.example.nexus.ui.features

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nexus.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val _isListening = MutableLiveData<Boolean>()
    val isListening: LiveData<Boolean> = _isListening

    private val _ledStatus = MutableLiveData<Map<String, String>>()
    val ledStatus: LiveData<Map<String, String>> = _ledStatus

    private val _lastCommand = MutableLiveData<Pair<String, String>>()
    val lastCommand: LiveData<Pair<String, String>> = _lastCommand

    private val _lastMessage = MutableLiveData<Pair<String, String>>()
    val lastMessage: LiveData<Pair<String, String>> = _lastMessage

    private var isPolling = false

    fun startPolling() {
        isPolling = true
        viewModelScope.launch {
            while (isPolling) {
                try {
                    val api = RetrofitClient.getClient(getApplication())
                    
                    val voiceStatus = api.getVoiceStatus()
                    _isListening.postValue(voiceStatus["listening"] as? Boolean ?: false)

                    val ledStatus = api.getLedStatus()
                    _ledStatus.postValue(ledStatus)

                    val cmd = api.getLastCommand()
                    _lastCommand.postValue((cmd["command"] ?: "None") to (cmd["timestamp"] ?: "--:--"))

                    val msg = api.getLastMessage()
                    _lastMessage.postValue((msg["message"] ?: "None") to (msg["timestamp"] ?: "--:--"))

                } catch (e: Exception) {
                    // Handle error
                }
                delay(3000)
            }
        }
    }

    fun stopPolling() {
        isPolling = false
    }

    fun activateVoiceMode() {
        viewModelScope.launch {
            try {
                RetrofitClient.getClient(getApplication()).activateVoice()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}