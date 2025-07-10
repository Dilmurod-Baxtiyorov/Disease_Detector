package com.example.diseasedetector.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T: ViewModel> viewModelFactory(creator: () -> T): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <T2 : ViewModel> create(modelClass: Class<T2>): T2 {
            @Suppress("UNCHECKED_CAST")
            return creator() as T2
        }
    }