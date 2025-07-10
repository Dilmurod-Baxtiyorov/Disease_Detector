package com.example.diseasedetector.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.model.User
import com.example.diseasedetector.navigation.Routes
import com.example.diseasedetector.repository.AuthRepository
import com.example.diseasedetector.ui.state.UiEvent
import com.example.handybook.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(auth: FirebaseAuth, db: FirebaseFirestore): ViewModel() {
    val repository = AuthRepository(auth, db)

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

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
        _password.value = newPassword
    }

    fun checkTextFields(): Boolean{
        return !(password.value == "" || phoneNumber.value == "").also { warning = "empty" }
    }

    fun login(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val user = repository.getUserByPhoneNumber(phoneNumber.value)
            if(user != null){
                if(user.password == password.value){
                    /*navigate to the main screen*/
                    _uiState.value = UiState.Success
                }else{
                    warning = "password"
                    _uiState.value = UiState.Idle
                }
            }else{
                warning = "wrong"
                _uiState.value = UiState.Idle
            }
        }
    }


}