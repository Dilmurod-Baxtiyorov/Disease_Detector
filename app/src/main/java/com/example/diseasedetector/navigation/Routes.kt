package com.example.diseasedetector.navigation

enum class Routes {
    Login,
    Signup,
    Verification,
    NewPassword
}

sealed class Screen(val route: String){
    data object Login: Screen(Routes.Login.name)
    data object Signup: Screen(Routes.Signup.name)
    data object Verification: Screen(Routes.Verification.name)
    data object NewPassword: Screen(Routes.NewPassword.name)
}