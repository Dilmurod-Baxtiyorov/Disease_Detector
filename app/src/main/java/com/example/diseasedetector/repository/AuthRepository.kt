package com.example.diseasedetector.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.diseasedetector.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth, db: FirebaseFirestore) {

    private val userRef = db.collection("users")

    suspend fun signup(credential: PhoneAuthCredential): String? {
        return try {
            auth.signInWithCredential(credential).await().user?.uid
        }catch (e: Exception){
            Log.d(TAG, "signup: ${e.message}")
            null
        }
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

    suspend fun getUserByPhoneNumber(phoneNumber: String): User? {
        return try {
            userRef
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .await()
                .toObjects<User>()
                .first()
        }catch (e: Exception){
            Log.d(TAG, "getUserBuPhoneNumber: ${e.message}")
            null
        }
    }

    suspend fun setNewPassword(uId: String, newPassword: String): String{
        try {
            userRef
                .document(uId)
                .update("password", newPassword)
                .await()
            return "Password changed to $newPassword"
        }catch (e: Exception){
            Log.d(TAG, "changePassword: ${e.message}")
            return "Password change failed ${e.message}"
        }
    }
}