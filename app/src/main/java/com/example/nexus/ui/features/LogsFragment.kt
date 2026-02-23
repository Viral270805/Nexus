package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.nexus.databinding.FragmentFeatureLogsBinding

class LogsFragment : Fragment() {

    private var _binding: FragmentFeatureLogsBinding? = null
    private val binding get() = _binding!!

    // Dummy data for logs
    private val logs = arrayOf(
        "LOG [8:15 PM]: Motion detected at Front Door",
        "LOG [8:10 PM]: User 'Demo User' turned ON Living Room Fan",
        "LOG [8:01 PM]: Unknown face detected at Garage",
        "LOG [7:30 PM]: Back window opened",
        "LOG [7:00 PM]: System armed by User 'Demo User'",
        "LOG [6:55 PM]: User 'Demo User' logged in"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, logs)
        binding.listViewLogs.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}