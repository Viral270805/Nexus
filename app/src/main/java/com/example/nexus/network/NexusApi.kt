package com.example.nexus.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface NexusApi {
    @GET("api/ping")
    suspend fun ping(): Map<String, Any>

    @GET("api/status")
    suspend fun getStatus(): SystemStatus

    @GET("api/motion/status")
    suspend fun getMotionStatus(): Map<String, Any>

    @GET("api/gesture/status")
    suspend fun getGestureStatus(): Map<String, Any>

    @GET("api/voice/status")
    suspend fun getVoiceStatus(): Map<String, Any>

    @GET("api/voice/last_command")
    suspend fun getLastCommand(): Map<String, String>

    @GET("api/voice/last_message")
    suspend fun getLastMessage(): Map<String, String>

    @GET("api/led/status")
    suspend fun getLedStatus(): Map<String, String>

    @GET("api/alerts")
    suspend fun getAlerts(): List<Alert>

    @GET("api/alerts/count")
    suspend fun getAlertCount(): Map<String, Int>

    @GET("api/alerts/summary")
    suspend fun getAlertSummary(): AlertSummary

    @GET("api/logs")
    suspend fun getLogs(): List<LogEntry>

    @GET("api/logs/latest")
    suspend fun getLatestLogs(): List<LogEntry>

    @GET("api/connection/status")
    suspend fun getConnectionStatus(): Map<String, Any>

    @POST("api/voice/activate")
    suspend fun activateVoice(): Map<String, Any>

    @POST("api/motion/start")
    suspend fun startMotionDetection(): Map<String, Any>

    @POST("api/motion/sensitivity")
    suspend fun setMotionSensitivity(@Body body: Map<String, Int>): Map<String, Any>

    @POST("api/gesture/toggle")
    suspend fun toggleGesture(@Body body: Map<String, Boolean>): Map<String, Any>

    @POST("api/gesture/sensitivity")
    suspend fun setGestureSensitivity(@Body body: Map<String, Int>): Map<String, Any>

    @POST("api/mic/gain")
    suspend fun setMicGain(@Body body: Map<String, Int>): Map<String, Any>

    @POST("api/buzzer/toggle")
    suspend fun toggleBuzzer(@Body body: Map<String, Boolean>): Map<String, Any>

    @POST("api/speaker/toggle")
    suspend fun toggleSpeaker(@Body body: Map<String, Boolean>): Map<String, Any>

    @POST("api/alerts/motion/toggle")
    suspend fun toggleMotionAlerts(@Body body: Map<String, Boolean>): Map<String, Any>

    @POST("api/logs/clear")
    suspend fun clearLogs(): Map<String, Any>

    @POST("api/camera/capture")
    suspend fun captureFrame(): Map<String, Any>
}

data class SystemStatus(
    @SerializedName("status") val status: String? = "offline",
    @SerializedName("pir_active") val pir_active: Boolean = false,
    @SerializedName("camera_active") val camera_active: Boolean = false,
    @SerializedName("mic_listening") val mic_listening: Boolean = false
)

data class Alert(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("tag") val tag: String
)

data class AlertSummary(
    @SerializedName("motion") val motion: Int = 0,
    @SerializedName("gesture") val gesture: Int = 0,
    @SerializedName("voice") val voice: Int = 0
)

data class LogEntry(
    @SerializedName("time") val time: String,
    @SerializedName("description") val description: String,
    @SerializedName("tag") val tag: String
)