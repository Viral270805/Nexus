package com.example.nexus.ui.features

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexus.R
import com.example.nexus.databinding.ItemAlertBinding
import com.example.nexus.network.Alert

class AlertAdapter : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    private var alerts: List<Alert> = listOf()

    fun setAlerts(newAlerts: List<Alert>) {
        alerts = newAlerts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(alerts[position])
    }

    override fun getItemCount(): Int = alerts.size

    class AlertViewHolder(private val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alert: Alert) {
            binding.tvTitle.text = alert.type
            binding.tvDescription.text = alert.description
            binding.tvTimestamp.text = alert.timestamp
            binding.tvTag.text = alert.tag

            val color = when (alert.tag.lowercase()) {
                "pir", "cam" -> itemView.context.getColor(R.color.red_alert)
                "gesture" -> itemView.context.getColor(R.color.blue_gesture)
                "mic" -> itemView.context.getColor(R.color.yellow_voice)
                "sys" -> itemView.context.getColor(R.color.green_success)
                else -> itemView.context.getColor(R.color.secondary_text)
            }

            binding.sideBar.setBackgroundColor(color)
            binding.tvTag.setTextColor(color)
            binding.ivIcon.setColorFilter(color)
            
            binding.ivIcon.setImageResource(when(alert.tag.lowercase()) {
                "pir", "cam" -> R.drawable.ic_notifications
                "gesture" -> R.drawable.ic_back_hand
                "mic" -> R.drawable.ic_mic
                else -> R.drawable.ic_monitor_heart
            })
        }
    }
}