package com.example.nexus.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nexus.R
import com.example.nexus.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.cardMotion.setOnClickListener { findNavController().navigate(R.id.nav_alerts) }
        binding.cardGesture.setOnClickListener { findNavController().navigate(R.id.nav_camera) }
        binding.cardVoice.setOnClickListener { findNavController().navigate(R.id.nav_voice) }
        binding.cardCamera.setOnClickListener { findNavController().navigate(R.id.nav_camera) }
        binding.cardAlerts.setOnClickListener { findNavController().navigate(R.id.nav_alerts) }
        binding.cardStatus.setOnClickListener { findNavController().navigate(R.id.nav_settings) }
    }

    private fun observeViewModel() {
        viewModel.systemStatus.observe(viewLifecycleOwner) { status ->
            val isOnline = status?.status == "online"
            
            // Top Status & Dashboard Cards
            if (isOnline) {
                binding.dotTopStatus.setBackgroundResource(R.drawable.status_dot_green)
                binding.tvTopStatus.text = "System Online"
                binding.tvTopStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_success))
                
                binding.statusSystem.text = "Online"
                binding.dotSystemStatus.setBackgroundResource(R.drawable.status_dot_green)
                
                binding.dotPir.setBackgroundResource(if (status!!.pir_active) R.drawable.status_dot_green else R.drawable.status_dot_red)
                binding.tvPirStatus.text = if (status.pir_active) "PIR Active" else "PIR Inactive"
                
                binding.statusMotion.text = if (status.pir_active) "Monitoring" else "Inactive"
                binding.dotMotion.setBackgroundResource(if (status.pir_active) R.drawable.status_dot_green else R.drawable.status_dot_grey)
            } else {
                binding.dotTopStatus.setBackgroundResource(R.drawable.status_dot_grey)
                binding.tvTopStatus.text = "System Offline"
                binding.tvTopStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary_text))
                
                binding.statusSystem.text = "Offline"
                binding.dotSystemStatus.setBackgroundResource(R.drawable.status_dot_grey)
                
                binding.dotPir.setBackgroundResource(R.drawable.status_dot_grey)
                binding.tvPirStatus.text = "PIR Offline"
                binding.statusMotion.text = "Offline"
                binding.dotMotion.setBackgroundResource(R.drawable.status_dot_grey)
            }
        }

        viewModel.alertCount.observe(viewLifecycleOwner) { count ->
            binding.statusAlerts.text = if (count > 0) "$count Alerts Found" else "No Alerts"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startPolling()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPolling()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}