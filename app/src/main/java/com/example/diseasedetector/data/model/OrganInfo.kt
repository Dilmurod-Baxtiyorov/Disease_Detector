package com.example.diseasedetector.data.model

data class OrganInfo(
    val id: Int,
    val name: String,
    val imageResId: Int,
    val diseases: List<String>
)