package com.example.nexus.network

import android.content.Context
import com.example.nexus.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var nexusApi: NexusApi? = null
    private var currentIp: String? = null

    fun getClient(context: Context): NexusApi {
        val sessionManager = SessionManager(context)
        val ip = sessionManager.getRPiIP() ?: "10.51.173.82"

        if (nexusApi == null || ip != currentIp) {
            currentIp = ip
            val baseUrl = "http://$ip:5000/"

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

            nexusApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NexusApi::class.java)
        }
        
        return nexusApi!!
    }
}