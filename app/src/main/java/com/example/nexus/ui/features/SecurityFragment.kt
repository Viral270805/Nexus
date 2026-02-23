package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nexus.databinding.FragmentFeatureSecurityBinding

class SecurityFragment : Fragment() {

    private var _binding: FragmentFeatureSecurityBinding? = null
    private val binding get() = _binding!!

    // Dummy data for alerts
    private val alerts = arrayOf(
        "Alert: Motion detected at Front Door (8:15 PM)",
        "Alert: Unknown face detected at Garage (8:01 PM)",
        "Warning: Back window open (7:30 PM)",
        "Info: System armed (7:00 PM)"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureSecurityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the list adapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, alerts)
        binding.listViewAlerts.adapter = adapter

        binding.switchBuzzer.setOnCheckedChangeListener { _, isChecked ->
            val state = if (isChecked) "ON" else "OFF"
            Toast.makeText(context, "Buzzer turned $state", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}