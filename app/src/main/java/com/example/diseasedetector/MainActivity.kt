package com.example.diseasedetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.navigation.AppNavHost
import com.example.diseasedetector.ui.theme.DiseaseDetectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = DiseaseViewModel(application)
        setContent {
            DiseaseDetectorTheme {
                AppNavHost(
                    rememberNavController(),
                    viewModel = viewModel
                )
            }
        }
    }
}