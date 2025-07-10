package com.example.diseasedetector.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.diseasedetector.ui.screens.Login
import com.example.diseasedetector.ui.screens.NewPassword
import com.example.diseasedetector.ui.screens.Signup
import com.example.diseasedetector.ui.screens.Verification
import com.example.diseasedetector.viewmodel.AuthViewModel
import com.example.diseasedetector.viewmodel.LoginViewModel
import com.example.diseasedetector.viewmodel.NewPasswordViewModel
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
    startDestination: String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        val authViewModel = AuthViewModel(auth, db)
        composable(Routes.Login.name){
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
            route = Routes.Verification.name + "/{phoneNumber}/{verificationId}/{fullName}/{password}"){ navBackStackEntry ->
            val verificationId = navBackStackEntry.arguments?.getString("verificationId")
            val phoneNumber = navBackStackEntry.arguments?.getString("phoneNumber")
            val fullName = navBackStackEntry.arguments?.getString("fullName")
            val password = navBackStackEntry.arguments?.getString("password")

            val credential = authViewModel.storedCredential
            val resendToken = authViewModel.resendingToken

            val verificationViewModel: VerificationViewModel = viewModel(
                factory = remember(auth, db, verificationId, phoneNumber, fullName, password, credential) {
                    viewModelFactory {
                        VerificationViewModel(
                            auth = auth,
                            db = db,
                            verificationId = verificationId,
                            phoneNumber = phoneNumber!!,
                            fullName = fullName,
                            password = password,
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
        composable(Routes.NewPassword.name + "/{phoneNumber}"){navBackStackEntry ->
            val phoneNumber = navBackStackEntry.arguments?.getString("phoneNumber")

            val newPasswordViewmodel : NewPasswordViewModel = viewModel(
                factory = remember (auth, db){
                    viewModelFactory {
                        NewPasswordViewModel(auth, db)
                    }
                }
            )

            NewPassword(
                navController = navController,
                vm = newPasswordViewmodel,
                phoneNumber = phoneNumber!!
            )
        }
    }
}