package com.example.diseasedetector.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DiseaseViewModel(application: Application) : AndroidViewModel(application) {
    private val _isChatSelected = MutableStateFlow<Boolean?>(null) // Nullable initially
    val isChatSelected: StateFlow<Boolean?> = _isChatSelected

    fun setChatSelected(selected: Boolean) {
        _isChatSelected.value = selected
    }

    fun initializeIfNeeded(isChatStart: Boolean) {
        if (_isChatSelected.value == null) {
            _isChatSelected.value = isChatStart
        }
    }
}