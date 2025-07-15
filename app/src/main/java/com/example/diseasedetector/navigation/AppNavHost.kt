package com.example.diseasedetector.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diseasedetector.model.DiseaseViewModel
import com.example.diseasedetector.ui.screens.AnalysisScreen
import com.example.diseasedetector.ui.screens.ChatScreen
import com.example.diseasedetector.ui.screens.MainScreen
import com.example.diseasedetector.ui.screens.SplashScreen
import com.example.diseasedetector.ui.screens.Login
import com.example.diseasedetector.ui.screens.Signup
import com.example.diseasedetector.ui.screens.Verification
import com.example.diseasedetector.viewmodel.AuthViewModel
import com.example.diseasedetector.viewmodel.LoginViewModel
import com.example.diseasedetector.viewmodel.SignupViewModel
import com.example.diseasedetector.viewmodel.VerificationViewModel
import com.example.diseasedetector.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AppNavHost(
    navController: NavHostController,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    startDestination: String = NavigationItem.SplashScreen.route,
    viewModel: DiseaseViewModel = viewModel()
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        val authViewModel = AuthViewModel(auth, db)
        composable(Routes.Login.name){
            authViewModel.clearWarning()
            val loginViewModel: LoginViewModel = viewModel(
                factory = remember(auth, db) {
                    viewModelFactory {
                        LoginViewModel(auth, db)
                    }
                }
            )
            Login(
                navController = navController,
                vm = loginViewModel,
                authVM = authViewModel
            )
        }
        
        composable(Routes.Signup.name){
            authViewModel.clearWarning()
            val signupViewModel: SignupViewModel = viewModel(
                factory = remember(auth, db) {
                    viewModelFactory {
                        SignupViewModel(auth, db)
                    }
                }
            )
            Signup(
                navController = navController,
                vm = signupViewModel,
                authVM = authViewModel
            )
        }
        
        composable(
            route = Routes.Verification.name + "/{identifier}/{verificationId}/{fullName}"){ navBackStackEntry ->
            val verificationId = navBackStackEntry.arguments?.getString("verificationId")
            val phoneNumber = navBackStackEntry.arguments?.getString("identifier")
            val fullName = navBackStackEntry.arguments?.getString("fullName")

            val credential = authViewModel.storedCredential
            val resendToken = authViewModel.resendingToken
            authViewModel.clearWarning()

            val verificationViewModel: VerificationViewModel = viewModel(
                factory = remember(auth, db, verificationId, phoneNumber, fullName, credential) {
                    viewModelFactory {
                        VerificationViewModel(
                            auth = auth,
                            db = db,
                            verificationId = verificationId,
                            phoneNumber = phoneNumber!!,
                            fullName = fullName,
                            credential = credential,
                            resendingToken = resendToken
                        )
                    }
                }
            )

            Verification(
                vm = verificationViewModel,
                navController = navController,
                phoneNumber = phoneNumber!!
            )
        }
        
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