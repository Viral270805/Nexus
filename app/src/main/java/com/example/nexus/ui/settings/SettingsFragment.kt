package com.example.nexus.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nexus.R
import com.example.nexus.databinding.FragmentSettingsBinding
import com.example.nexus.utils.SessionManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        setupConnectionSettings()
        setupMotionSettings()
        setupGestureSettings()
        setupAudioSettings()
        observeViewModel()
    }

    private fun setupConnectionSettings() {
        binding.etRpiIp.setText(sessionManager.getRPiIP())
        binding.btnConnect.setOnClickListener {
            val ip = binding.etRpiIp.text.toString()
            if (ip.isNotEmpty()) {
                sessionManager.saveRPiIP(ip)
                viewModel.testConnection()
            }
        }
    }

    private fun setupMotionSettings() {
        binding.sbMotionSensitivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvMotionSensitivityValue.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.setMotionSensitivity(seekBar?.progress ?: 1)
            }
        })

        binding.swMotionAlerts.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleMotionAlerts(isChecked)
        }
    }

    private fun setupGestureSettings() {
        binding.sbGestureSensitivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.setGestureSensitivity(seekBar?.progress ?: 1)
            }
        })

        binding.swGestureMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGesture(isChecked)
        }
    }

    private fun setupAudioSettings() {
        binding.sbMicGain.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvMicGainValue.text = "${progress}x"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.setMicGain(seekBar?.progress ?: 1)
            }
        })

        binding.swBuzzer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleBuzzer(isChecked)
        }

        binding.swSpeaker.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleSpeaker(isChecked)
        }
    }

    private fun observeViewModel() {
        viewModel.connectionResult.observe(viewLifecycleOwner) { result ->
            binding.tvConnectionResult.visibility = View.VISIBLE
            binding.tvConnectionResult.text = result
            val color = if (viewModel.isSuccess.value == true) R.color.green_success else R.color.red_alert
            binding.tvConnectionResult.setTextColor(resources.getColor(color, null))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}