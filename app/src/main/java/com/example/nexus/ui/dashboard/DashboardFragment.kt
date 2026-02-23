package com.example.nexus.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nexus.R
import com.example.nexus.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners for each card

        binding.cardVoice.setOnClickListener {
            findNavController().navigate(R.id.nav_voice)
        }

        binding.cardAppliance.setOnClickListener {
            findNavController().navigate(R.id.nav_appliance)
        }

        binding.cardAutomation.setOnClickListener {
            findNavController().navigate(R.id.nav_automation)
        }

        binding.cardCamera.setOnClickListener {
            findNavController().navigate(R.id.nav_camera)
        }

        binding.cardSecurity.setOnClickListener {
            findNavController().navigate(R.id.nav_security)
        }

        binding.cardLogs.setOnClickListener {
            findNavController().navigate(R.id.nav_logs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}