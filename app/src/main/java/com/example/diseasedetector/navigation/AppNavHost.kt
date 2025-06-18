package com.example.diseasedetector.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.ui.screens.AnalysisScreen
import com.example.diseasedetector.ui.screens.MainScreen
import com.example.diseasedetector.ui.screens.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.SplashScreen.route,
    viewModel: DiseaseViewModel
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.SplashScreen.route) {
            SplashScreen(navController)
        }

        composable(NavigationItem.MainScreen.route) {
            MainScreen(navController)
        }

        composable(NavigationItem.AnalysisScreen.route) {
            AnalysisScreen(navController)
        }

    }
}