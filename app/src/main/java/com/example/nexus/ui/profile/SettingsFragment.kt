package com.example.nexus.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.nexus.databinding.FragmentSettingsBinding
import com.example.nexus.network.RetrofitClient
import com.example.nexus.utils.SessionManager
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.NetworkInterface

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext())
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.etRpiIp.setText(sessionManager.getRPiIP())
        
        // Initialize Toggles from Persistence
        binding.swMotionAlerts.isChecked = sessionManager.areMotionAlertsEnabled()
        binding.swGestureMode.isChecked = sessionManager.isGestureModeEnabled()
        binding.swBuzzer.isChecked = sessionManager.areBuzzerAlertsEnabled()
        binding.swSpeaker.isChecked = sessionManager.isSpeakerEnabled()
        binding.switchDarkMode.isChecked = sessionManager.isDarkMode()

        // Toggle Listeners with API Calls
        binding.swMotionAlerts.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setMotionAlerts(isChecked)
            syncToggleWithHardware("motion_alerts", isChecked)
        }
        binding.swGestureMode.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setGestureMode(isChecked)
            syncToggleWithHardware("gesture", isChecked)
        }
        binding.swBuzzer.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setBuzzerAlerts(isChecked)
            syncToggleWithHardware("buzzer", isChecked)
        }
        binding.swSpeaker.setOnCheckedChangeListener { _, isChecked ->
            sessionManager.setSpeakerEnabled(isChecked)
            syncToggleWithHardware("speaker", isChecked)
        }

        binding.btnConnect.setOnClickListener {
            val ip = binding.etRpiIp.text.toString().trim()
            if (ip.isNotEmpty()) {
                sessionManager.saveRPiIP(ip)
                testConnection(ip)
            }
        }
        displayNetworkInfo()
    }

    private fun syncToggleWithHardware(type: String, enabled: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val api = RetrofitClient.getClient(requireContext())
                val body = mapOf("enabled" to enabled)
                when (type) {
                    "motion_alerts" -> api.toggleMotionAlerts(body)
                    "gesture" -> api.toggleGesture(body)
                    "buzzer" -> api.toggleBuzzer(body)
                    "speaker" -> api.toggleSpeaker(body)
                }
            } catch (e: Exception) {
                // Silent fail for toggle sync to avoid UI jitter
            }
        }
    }

    private fun displayNetworkInfo() {
        val phoneIp = getLocalIpAddress()
        binding.tvConnectionResult.apply {
            visibility = View.VISIBLE
            text = "Phone IP: $phoneIp\nTarget RPi: ${sessionManager.getRPiIP()}"
            setTextColor(Color.LTGRAY)
        }
    }

    private fun getLocalIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is Inet4Address) return address.hostAddress ?: ""
                }
            }
        } catch (e: Exception) {}
        return "Not Connected"
    }

    private fun testConnection(ip: String) {
        binding.tvConnectionResult.apply {
            visibility = View.VISIBLE
            text = "Testing connection to $ip..."
            setTextColor(Color.YELLOW)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitClient.getClient(requireContext()).getStatus()
                binding.tvConnectionResult.apply {
                    text = "Success! System is ${response.status}"
                    setTextColor(Color.GREEN)
                }
            } catch (e: Exception) {
                binding.tvConnectionResult.apply {
                    text = "Failed: ${e.localizedMessage}"
                    setTextColor(Color.RED)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}