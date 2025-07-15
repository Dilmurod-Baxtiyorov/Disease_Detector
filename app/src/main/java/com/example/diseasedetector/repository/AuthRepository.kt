package com.example.diseasedetector.repository

import android.content.ContentValues.TAG
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.util.Log
import com.example.diseasedetector.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import java.time.Instant

class AuthRepository(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {

    private val userRef = db.collection("users")
    private val quotaRef = db.collection("smsQuota").document("global")

    suspend fun signup(credential: PhoneAuthCredential): String? {
        return try {
            auth.signInWithCredential(credential).await().user?.uid
        }catch (e: Exception){
            Log.d(TAG, "signup: ${e.message}")
            null
        }
    }

    suspend fun createEmailAccount(email: String, password: String): FirebaseUser? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await().user
        }catch (e: Exception){
            Log.d(TAG, "createEmailAccount: ${e.message}")
            null
        }
    }

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await().user
        }catch (e: Exception){
            Log.d(TAG, "signInWithEmail: ${e.message}")
            null
        }
    }

    suspend fun resetEmailPassword(email: String){
        try {
            auth.sendPasswordResetEmail(email).await()
        }catch (e: Exception){
            Log.d(TAG, "resetEmailPassword: ${e.message}")

        }
    }

    suspend fun verifyEmail(user: FirebaseUser){
        user.sendEmailVerification()
            .addOnSuccessListener {
                Log.d(TAG, "verifyEmail: Successful")
            }.await()
    }

    suspend fun saveUser(user: User) {
        try {
            userRef
                .document(user.id)
                .set(user)
                .await()
        } catch(e: Exception) {
            Log.d(TAG, "saveUser: ${e.message}")
        }
    }

    suspend fun getUserByIdentifier(identifier: String): User? {
        return try {
            userRef
                .whereEqualTo("identifier", identifier)
                .get()
                .await()
                .toObjects<User>()
                .first()
        }catch (e: Exception){
            Log.d(TAG, "getUserBuPhoneNumber: ${e.message}")
            null
        }
    }

    suspend fun incrementGlobalSmsCountIfAllowed(): Boolean {
        return try {

            db.runTransaction { transaction ->
                val snapshot = transaction.get(quotaRef)

                val nowUtc = Instant.now().toEpochMilli()
                val expiresAtMillis = snapshot.getTimestamp("expiresAt")?.toDate()?.time ?: 0L
                val count = snapshot.getLong("count") ?: 0

                if (expiresAtMillis < nowUtc || !snapshot.exists()) {
                    val nextReset = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                        timeInMillis = nowUtc
                        set(Calendar.HOUR_OF_DAY, 8)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                        if (timeInMillis <= nowUtc) {
                            add(Calendar.DATE, 1)
                        }
                    }

                    transaction.set(quotaRef, mapOf(
                        "count" to 1,
                        "expiresAt" to Timestamp(nextReset.time)
                    ))

                    true
                } else {
                    if (count >= 10) {
                        false
                    } else {
                        transaction.update(quotaRef, "count", count + 1)
                        true
                    }
                }
            }.await()
        } catch (e: Exception) {
            Log.e("SMSQuota", "Error checking quota", e)
            false
        }
    }

}