package com.example.diseasedetector.ui.state

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    object Success: UiState()
    data class Error(val msg: String): UiState()
}