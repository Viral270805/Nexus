package com.example.nexus.ui.features

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nexus.R
import com.example.nexus.databinding.FragmentActivityLogsBinding
import com.example.nexus.utils.SessionManager

class ActivityLogsFragment : Fragment() {

    private var _binding: FragmentActivityLogsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActivityLogsViewModel by viewModels()
    private val adapter = LogAdapter()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        setupMenu()
        setupRecyclerView()
        observeViewModel()

        binding.tvRpiIp.text = "RPi IP: ${sessionManager.getRPiIP() ?: "Not set"}"
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_logs, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_clear -> {
                        viewModel.clearLogs()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.rvLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLogs.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.logs.observe(viewLifecycleOwner) { logs ->
            adapter.setLogs(logs)
            binding.tvTotalEvents.text = "Total Events: ${logs.size}"
            if (binding.swAutoScroll.isChecked && logs.isNotEmpty()) {
                binding.rvLogs.smoothScrollToPosition(logs.size - 1)
            }
        }

        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            binding.tvConnectionStatus.text = if (isConnected) "System Connected" else "Connection Lost"
            binding.dotConnection.setBackgroundResource(if (isConnected) R.drawable.status_dot_green else R.drawable.status_dot_red)
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