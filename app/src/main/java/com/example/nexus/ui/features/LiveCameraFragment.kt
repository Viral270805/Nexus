package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nexus.R
import com.example.nexus.databinding.FragmentLiveCameraBinding
import com.example.nexus.utils.SessionManager

class LiveCameraFragment : Fragment() {

    private var _binding: FragmentLiveCameraBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LiveCameraViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLiveCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupCameraStream()
        setupControls()
        observeViewModel()

        viewModel.fetchStatus()
    }

    private fun setupCameraStream() {
        val ip = sessionManager.getRPiIP() ?: "10.51.173.82"
        val streamUrl = "http://$ip:5000/api/camera/stream"

        binding.webViewCamera.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.tvCameraLoading.visibility = View.GONE
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    binding.tvCameraLoading.text = "Connection Failed"
                }
            }

            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
            }

            loadUrl(streamUrl)
        }
    }

    private fun setupControls() {
        binding.sbMotionSensitivity.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    viewModel.setMotionSensitivity(seekBar?.progress ?: 1)
                }
            }
        )

        binding.swGesture.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleGesture(isChecked)
        }

        binding.btnStartDetection.setOnClickListener {
            viewModel.startDetection()
        }

        binding.btnCaptureFrame.setOnClickListener {
            viewModel.captureFrame()
        }
    }

    private fun observeViewModel() {
        viewModel.motionStatus.observe(viewLifecycleOwner) { isActive ->
            binding.statusMotion.text =
                if (isActive) "Active" else "Inactive"

            binding.dotMotion.setBackgroundResource(
                if (isActive)
                    R.drawable.status_dot_green
                else
                    R.drawable.status_dot_grey
            )
        }

        viewModel.gestureStatus.observe(viewLifecycleOwner) { isEnabled ->
            binding.swGesture.isChecked = isEnabled
            binding.statusGesture.text =
                if (isEnabled) "Active" else "Standby"

            binding.dotGesture.setBackgroundResource(
                if (isEnabled)
                    R.drawable.status_dot_blue
                else
                    R.drawable.status_dot_grey
            )
        }

        viewModel.lastMotion.observe(viewLifecycleOwner) { time ->
            binding.tvLastMotion.text = time
        }
    }

    override fun onDestroyView() {
        binding.webViewCamera.destroy()
        _binding = null
        super.onDestroyView()
    }
}