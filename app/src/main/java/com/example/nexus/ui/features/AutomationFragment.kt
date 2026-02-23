package com.example.nexus.ui.features

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nexus.databinding.FragmentFeatureAutomationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AutomationFragment : Fragment() {

    private var _binding: FragmentFeatureAutomationBinding? = null
    private val binding get() = _binding!!

    private var selectedTime: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureAutomationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSelectedTimeText()

        binding.btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSaveAutomation.setOnClickListener {
            val task = "Turn on Living Room Lights" // Dummy task
            val time = binding.tvSelectedTime.text.toString()
            Toast.makeText(context, "Automation Saved: $task at $time", Toast.LENGTH_LONG).show()
        }
    }

    private fun showTimePicker() {
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                updateSelectedTimeText()
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            false // Use 12-hour format
        )
        timePicker.show()
    }

    private fun updateSelectedTimeText() {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
        binding.tvSelectedTime.text = format.format(selectedTime.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}