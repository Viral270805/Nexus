package com.example.nexus.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nexus.databinding.FragmentFeatureApplianceBinding
import com.google.android.material.materialswitch.MaterialSwitch

class ApplianceFragment : Fragment() {

    private var _binding: FragmentFeatureApplianceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureApplianceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set listeners for all switches
        val switchClickListener = View.OnClickListener {
            val switch = it as MaterialSwitch
            val deviceName = switch.text
            val state = if (switch.isChecked) "ON" else "OFF"
            Toast.makeText(context, "$deviceName turned $state", Toast.LENGTH_SHORT).show()
        }

        binding.switchLight.setOnClickListener(switchClickListener)
        binding.switchFan.setOnClickListener(switchClickListener)
        binding.switchAc.setOnClickListener(switchClickListener)
        binding.switchTv.setOnClickListener(switchClickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}