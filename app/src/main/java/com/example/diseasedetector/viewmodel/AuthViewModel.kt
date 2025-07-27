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
import com.example.diseasedetector.data.repository.AuthRepository
import com.example.diseasedetector.ui.state.UiEvent
import com.example.diseasedetector.ui.state.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel(private val auth: FirebaseAuth, private val db: FirebaseFirestore): ViewModel() {
    private val repository = AuthRepository(auth, db)

    var resendingToken by mutableStateOf<ForceResendingToken?>(null)
    var storedCredential by mutableStateOf<PhoneAuthCredential?>(null)

    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val  event = _event.asSharedFlow()

    private val _fullName = MutableStateFlow<String?>(null)
    val fullName: StateFlow<String?> = _fullName.asStateFlow()

    private val _phoneNumber = MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> = _phoneNumber.asStateFlow()

    var warning by mutableStateOf("")
        private set

    fun clearWarning(){
        warning = ""
    }

    fun verifyPhoneNumber(context: Context, phoneNumber: String, fullName: String? = null){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _phoneNumber.value = phoneNumber
            _fullName.value = fullName
            if(repository.incrementGlobalSmsCountIfAllowed()){
                if(fullName == null){
                    if(repository.getUserByIdentifier(phoneNumber) != null){
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(120L, TimeUnit.SECONDS)
                            .setActivity(context as Activity)
                            .setCallbacks(callbacks)
                        PhoneAuthProvider.verifyPhoneNumber(options.build())
                    }else{
                        warning = "exist"
                        _uiState.value = UiState.Idle
                    }
                }else{
                    if(repository.getUserByIdentifier(phoneNumber) == null){
                        val options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(120L, TimeUnit.SECONDS)
                            .setActivity(context as Activity)
                            .setCallbacks(callbacks)
                        PhoneAuthProvider.verifyPhoneNumber(options.build())
                    }else{
                        warning = "exist"
                        _uiState.value = UiState.Idle
                    }
                }
            }else{
                warning = "quota"
                _uiState.value = UiState.Idle
            }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            viewModelScope.launch {
                storedCredential = credential
                _event.emit(UiEvent.Navigate("${Routes.Verification.name}/${phoneNumber.value}/${null}/${fullName.value}"))
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {

            }

            _uiState.value = UiState.Error(e.message.toString())
        }
        override fun onCodeSent(
            verificationId: String,
            token: ForceResendingToken,
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            Log.d(TAG, "onCodeSent:$token")

            resendingToken = token

            viewModelScope.launch {
                _event.emit(UiEvent.Navigate("${Routes.Verification.name}/${phoneNumber.value}/$verificationId/${fullName.value}"))
            }

        }
    }
}