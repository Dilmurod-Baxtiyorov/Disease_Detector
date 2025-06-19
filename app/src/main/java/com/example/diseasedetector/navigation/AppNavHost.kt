package com.example.diseasedetector.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.ui.screens.AnalysisScreen
import com.example.diseasedetector.ui.screens.ChatScreen
import com.example.diseasedetector.ui.screens.MainScreen
import com.example.diseasedetector.ui.screens.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.SplashScreen.route,
    viewModel: DiseaseViewModel = viewModel()
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
            MainScreen(navController, viewModel)
        }

        composable("organ/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                AnalysisScreen(navController = navController, organId = id, viewModel = viewModel)
            }
        }

        composable(NavigationItem.ChatScreen.route) {
            ChatScreen(navController, viewModel)
        }

    }
}