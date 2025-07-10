package com.example.diseasedetector.viewmodel

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.model.User
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

class VerificationViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val verificationId: String?,
    private val phoneNumber: String,
    private val fullName: String?,
    private val password: String?,
    private val credential: PhoneAuthCredential?,
    private val resendingToken: ForceResendingToken?,
): ViewModel() {
    private val repository = AuthRepository(auth, db)

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    private val _event = MutableSharedFlow<UiEvent>()
    val  event = _event.asSharedFlow()

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code.asStateFlow()

    private var storedVerificationId by mutableStateOf(verificationId)

    private var resendToken by mutableStateOf(resendingToken)

    private var storedCredential by mutableStateOf(credential)

    var sms by mutableIntStateOf(1)
        private set

    var timer by mutableIntStateOf(120)
        private set


    fun timeOut(){
        if(timer > 0){
            timer--
        }
    }

    fun onCodeChange(newCode: String){
        if (newCode.length <= 6) {
            _code.value = newCode
        }
    }

    init {
        if(credential != null){
            signup()
        }
    }

    fun signup(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val getCredential = storedCredential ?: PhoneAuthProvider.getCredential(storedVerificationId!!, code.value)
            try {
                val uid = repository.signup(getCredential)
                if (uid != null) {
                    if(fullName == "null") {
                        _event.emit(UiEvent.Navigate("${Routes.NewPassword.name}/$phoneNumber"))
                    }else{
                        saveUser(User(uid.toString(), fullName!!, phoneNumber, password!!))

                    }
                    _uiState.value = UiState.Success
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun saveUser(user: User){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if(repository.getUserByPhoneNumber(user.phoneNumber) == null){
                repository.saveUser(user)
            }
        }
    }

    fun verifyPhoneNumber(context: Context){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (repository.getUserByPhoneNumber(phoneNumber) != null && fullName != null){
                Toast.makeText(context, "This phone number is already registered!!", Toast.LENGTH_LONG).show()
            }else{
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(120L, TimeUnit.SECONDS)
                    .setActivity(context as Activity)
                    .setCallbacks(callbacks)
                PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendToken!!).build())
            }
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(newCredential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$newCredential")
            storedCredential = newCredential
            signup()
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

            storedVerificationId = verificationId
            resendToken = token

            sms++

            timer = 120

            _uiState.value = UiState.Success
        }
    }

}