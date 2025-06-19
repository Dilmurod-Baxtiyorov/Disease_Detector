package com.example.diseasedetector.navigation

sealed class NavigationItem (val route: String){
    object SplashScreen: NavigationItem("splash")
    object MainScreen: NavigationItem("main")
    object AnalysisScreen: NavigationItem("analysis")
    object ChatScreen: NavigationItem("chat")
}