package com.example.diseasedetector.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.diseasedetector.R
import com.example.diseasedetector.data.model.OrganInfo

class DiseaseViewModel(application: Application) : AndroidViewModel(application) {
    private val _organs = listOf(
        OrganInfo(
            id = 1,
            name = "Lung",
            imageResId = R.drawable.or_lung,
            diseases = listOf("Tuberculosis", "Pneumonia", "Asthma")
        ),
        OrganInfo(
            id = 2,
            name = "Kidney",
            imageResId = R.drawable.or_kidney,
            diseases = listOf("Kidney Stones", "Nephritis")
        ),
        OrganInfo(
            id = 3,
            name = "Liver",
            imageResId = R.drawable.or_liver,
            diseases = listOf("Hepatitis", "Fatty Liver")
        ),
        OrganInfo(
            id = 4,
            name = "Heart",
            imageResId = R.drawable.or_heart,
            diseases = listOf("Heart Attack", "Arrhythmia", "Hypertension")
        ),
        OrganInfo(
            id = 5,
            name = "Brain",
            imageResId = R.drawable.or_brain,
            diseases = listOf("Stroke", "Alzheimer's Disease", "Brain Tumor")
        ),
        OrganInfo(
            id = 6,
            name = "Hand",
            imageResId = R.drawable.or_hand,
            diseases = listOf("Arthritis", "Carpal Tunnel Syndrome", "Tendinitis")
        )
    )

    val organList: List<OrganInfo> get() = _organs

    private val _isChatSelected = MutableStateFlow<Boolean?>(null)
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