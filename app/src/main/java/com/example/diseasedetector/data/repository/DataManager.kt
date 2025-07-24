package com.example.diseasedetector.data.repository

import android.content.Context
import androidx.activity.ComponentActivity

class DataManager(
    activity: ComponentActivity
) {
    private val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    fun saveUser(name: String){
        sharedPreferences
            .edit()
            .putString("name", name)
            .apply()
    }

    fun getUser(): String? {
        return sharedPreferences.getString("name", "")
    }
}