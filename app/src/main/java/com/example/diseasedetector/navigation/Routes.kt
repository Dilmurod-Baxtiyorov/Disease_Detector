package com.example.diseasedetector.navigation

enum class Routes {
    Splash,
    Login,
    Signup,
    Verification,
    Main,
    Analysis,
    Chat
}

sealed class Screen(val route: String){
    data object Login: Screen(Routes.Login.name)
    data object Signup: Screen(Routes.Signup.name)
    data object Verification: Screen(Routes.Verification.name)
    data object SplashScreen: Screen(Routes.Splash.name)
    data object MainScreen: Screen(Routes.Main.name)
    data object AnalysisScreen: Screen(Routes.Analysis.name)
    data object ChatScreen: Screen(Routes.Chat.name)
}