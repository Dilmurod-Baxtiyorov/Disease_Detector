package com.example.diseasedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.navigation.AppNavHost
import androidx.navigation.compose.rememberNavController
import com.example.diseasedetector.navigation.Routes
import com.example.diseasedetector.ui.theme.DiseaseDetectorTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = DiseaseViewModel(application)

        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")

        setContent {
            val navController = rememberNavController()
            DiseaseDetectorTheme {
                  AppNavHost(
                      navController = navController,
                      auth = auth,
                      db = db,
                      startDestination = Routes.Signup.name
                  )
            }
        }
    }
}