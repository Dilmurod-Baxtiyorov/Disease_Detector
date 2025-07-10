package com.example.diseasedetector.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.diseasedetector.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignupViewModel(private val auth: FirebaseAuth, private val db: FirebaseFirestore): ViewModel() {

    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    var warning by mutableStateOf("")
        private set

    fun onPhoneNumberChange(newPhoneNumber: String){
        _phoneNumber.value = newPhoneNumber
    }

    fun onPasswordChange(newPassword: String){
        if(newPassword.length <= 20){
            _password.value = newPassword
        }
    }

    fun onFullNameChange(newFullName: String){
        if(newFullName.length <= 40){
            _fullName.value = newFullName
        }
    }

    fun checkRegex(): Boolean{
        if(fullName.value.isEmpty()){
            warning = "fullName"
            return false
        }
        if (!phoneNumber.value.matches(Regex("^\\+998\\d{9}$"))){
            warning = "phoneNumber"
            return false
        }
        if(!password.value.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$"))){
            warning = "password"
            return false
        }
        warning = ""
        return true
    }
}