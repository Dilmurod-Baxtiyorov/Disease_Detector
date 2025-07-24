package com.example.diseasedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.diseasedetector.navigation.AppNavHost
import androidx.navigation.compose.rememberNavController
import com.example.diseasedetector.data.repository.DataManager
import com.example.diseasedetector.ui.theme.DiseaseDetectorTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")
        val dataManager = DataManager(this)
        setContent {
            val navController = rememberNavController()
            DiseaseDetectorTheme {
                  AppNavHost(
                      navController = navController,
                      auth = auth,
                      db = db,
                      dataManager = dataManager,
                      application = application
                  )
            }
        }
    }
}