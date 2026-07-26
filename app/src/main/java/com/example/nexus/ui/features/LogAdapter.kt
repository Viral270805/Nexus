package com.example.nexus.ui.features

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nexus.R
import com.example.nexus.databinding.ItemLogBinding
import com.example.nexus.network.LogEntry

class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    private var logs: List<LogEntry> = listOf()

    fun setLogs(newLogs: List<LogEntry>) {
        logs = newLogs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int = logs.size

    class LogViewHolder(private val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(log: LogEntry) {
            binding.tvLogTime.text = log.time
            binding.tvLogDescription.text = log.description
            binding.tvLogTag.text = log.tag

            val color = when (log.tag.lowercase()) {
                "pir" -> itemView.context.getColor(R.color.red_alert)
                "cam" -> itemView.context.getColor(R.color.blue_gesture)
                "mic" -> itemView.context.getColor(R.color.yellow_voice)
                "gesture" -> itemView.context.getColor(R.color.green_success)
                else -> itemView.context.getColor(R.color.secondary_text)
            }
            binding.tvLogTag.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color))
        }
    }
}