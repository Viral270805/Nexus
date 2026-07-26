package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nexus.R
import com.example.nexus.databinding.FragmentVoiceCommandBinding

class VoiceCommandFragment : Fragment() {

    private var _binding: FragmentVoiceCommandBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VoiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceCommandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnActivateVoice.setOnClickListener {
            viewModel.activateVoiceMode()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isListening.observe(viewLifecycleOwner) { isListening ->
            if (isListening) {
                binding.tvVoiceStatus.text = "Listening..."
                binding.tvVoiceStatus.setTextColor(resources.getColor(R.color.yellow_voice, null))
                startPulseAnimation()
            } else {
                binding.tvVoiceStatus.text = "Say NEXUS to activate"
                binding.tvVoiceStatus.setTextColor(resources.getColor(R.color.secondary_text, null))
                stopPulseAnimation()
            }
        }

        viewModel.ledStatus.observe(viewLifecycleOwner) { status ->
            val red = status["red"] ?: "OFF"
            val green = status["green"] ?: "OFF"
            binding.statusRedLed.text = red
            binding.statusGreenLed.text = green
            binding.dotRedLed.setBackgroundResource(if (red == "ON") R.drawable.status_dot_red else R.drawable.status_dot_grey)
            binding.dotGreenLed.setBackgroundResource(if (green == "ON") R.drawable.status_dot_green else R.drawable.status_dot_grey)
        }

        viewModel.lastCommand.observe(viewLifecycleOwner) { (cmd, time) ->
            binding.tvLastCommand.text = cmd
            binding.tvLastCommandTime.text = time
        }

        viewModel.lastMessage.observe(viewLifecycleOwner) { (msg, time) ->
            binding.tvLastMessage.text = msg
            binding.tvLastMessageTime.text = time
        }
    }

    private fun startPulseAnimation() {
        val anim = AlphaAnimation(1.0f, 0.2f)
        anim.duration = 1000
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        binding.pulseCircle.startAnimation(anim)
    }

    private fun stopPulseAnimation() {
        binding.pulseCircle.clearAnimation()
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