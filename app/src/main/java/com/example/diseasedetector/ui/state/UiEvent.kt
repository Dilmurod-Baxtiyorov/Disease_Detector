package com.example.diseasedetector.ui.state

sealed class UiEvent {
    data class Navigate(val route: String): UiEvent()
}