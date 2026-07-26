package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nexus.databinding.FragmentAlertHistoryBinding

class AlertHistoryFragment : Fragment() {

    private var _binding: FragmentAlertHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlertHistoryViewModel by viewModels()
    private val adapter = AlertAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlerts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAlerts.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.stopPolling()
            viewModel.startPolling()
        }

        observeViewModel()

        viewModel.startPolling()
    }

    private fun observeViewModel() {
        viewModel.alerts.observe(viewLifecycleOwner) { alerts ->
            adapter.setAlerts(alerts)
        }

        viewModel.summary.observe(viewLifecycleOwner) { summary ->
            binding.tvCountMotion.text = summary.motion.toString()
            binding.tvCountGesture.text = summary.gesture.toString()
            binding.tvCountVoice.text = summary.voice.toString()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }

    override fun onDestroyView() {
        viewModel.stopPolling()
        _binding = null
        super.onDestroyView()
    }
}