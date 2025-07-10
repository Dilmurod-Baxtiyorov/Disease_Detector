package com.example.diseasedetector.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.navigation.Routes
import com.example.diseasedetector.repository.AuthRepository
import com.example.diseasedetector.ui.state.UiEvent
import com.example.handybook.state.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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