package com.example.nexus.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getAlerts(): Flow<List<Alert>> = callbackFlow {
        val subscription = firestore.collection("alerts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val alerts = snapshot?.documents?.mapNotNull { doc ->
                    Alert(
                        id = doc.id,
                        type = doc.getString("type") ?: "",
                        description = doc.getString("description") ?: "",
                        timestamp = doc.getString("timestamp") ?: "",
                        tag = doc.getString("tag") ?: ""
                    )
                } ?: emptyList()
                trySend(alerts)
            }
        awaitClose { subscription.remove() }
    }

    fun getLogs(): Flow<List<LogEntry>> = callbackFlow {
        val subscription = firestore.collection("logs")
            .orderBy("time", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val logs = snapshot?.documents?.mapNotNull { doc ->
                    LogEntry(
                        time = doc.getString("time") ?: "",
                        description = doc.getString("description") ?: "",
                        tag = doc.getString("tag") ?: ""
                    )
                } ?: emptyList()
                trySend(logs)
            }
        awaitClose { subscription.remove() }
    }

    fun getPatientStatus(): Flow<PatientStatusFirebase> = callbackFlow {
        val subscription = firestore.collection("patient_status").document("current")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val status = snapshot?.toObject(PatientStatusFirebase::class.java) ?: PatientStatusFirebase()
                trySend(status)
            }
        awaitClose { subscription.remove() }
    }
}