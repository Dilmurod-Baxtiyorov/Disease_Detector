package com.example.diseasedetector.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.model.User
import com.example.diseasedetector.repository.AuthRepository
import com.example.diseasedetector.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignupViewModel(private val auth: FirebaseAuth, private val db: FirebaseFirestore): ViewModel() {
    private val repository = AuthRepository(auth, db)

    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user.asStateFlow()

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _identifier = MutableStateFlow("")
    val identifier: StateFlow<String> = _identifier.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    var warning by mutableStateOf("")
        private set

    fun onIdentifierChange(newIdentifier: String){
        _identifier.value = newIdentifier
    }

    fun onPasswordChange(newPassword: String){
        if(newPassword.length <= 20){
            _password.value = newPassword
        }
        checkPassword()
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
        if (!identifier.value.matches(Regex("^\\+998\\d{9}$")) && !identifier.value.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))){
            warning = "identifier"
            return false
        }
        warning = ""
        return true
    }

    fun checkPassword():Boolean{
        if(password.value.isEmpty() || password.value.length<6){
            warning = "password"
            return false
        }
        if(password.value.matches(Regex("^.{6,}$"))){
            warning = "weak"
        }
        if (password.value.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))){
            warning = "good"
        }
        if(password.value.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}$"))){
            warning = "strong"
        }
        return true
    }

    fun emailLogin(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if(repository.getUserByIdentifier(identifier.value) == null){
                val user = if(repository.signInWithEmail(email = identifier.value, password = password.value) == null){
                    repository.createEmailAccount(identifier.value, password.value)
                }else{
                    repository.signInWithEmail(identifier.value, password.value)
                }
                _user.value = user
                if(user != null){
                    repository.verifyEmail(user)
                }else{
                    warning = "unverified"
                    _uiState.value = UiState.Success
                }
            }else{
                warning = "exist"
                _uiState.value = UiState.Success
            }
        }
    }

    fun isEmailVerified(context: Context){
        viewModelScope.launch {
            var boolean = true
            while (boolean) {
                delay(3000)
                user.value?.reload()?.await()
                if(user.value?.isEmailVerified == true){
                    Log.d(TAG, "isEmailVerified: Successful registration")
                    repository.saveUser(
                        User(
                            id = user.value?.uid!!,
                            fullName = fullName.value,
                            identifier = identifier.value,
                        )
                    )
                    Toast.makeText(context, "Successful reg", Toast.LENGTH_SHORT).show()
                    _uiState.value = UiState.Success
                    /*Navigate to main screen*/
                    boolean = false
                }else{
                    Log.d(TAG, "isEmailVerified: Failed to verify email")
                }
            }
        }
    }
}