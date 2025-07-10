package com.example.diseasedetector.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.model.User
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

class NewPasswordViewModel(auth: FirebaseAuth, db: FirebaseFirestore): ViewModel() {
    private val repository = AuthRepository(auth, db)

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    private val _event = MutableSharedFlow<UiEvent>()
    val  event = _event.asSharedFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    var warning by mutableStateOf("")
        private set

    fun onNewPasswordChange(newPassword: String){
        _newPassword.value = newPassword
    }

    fun onConfirmPasswordChange(confirmPassword: String){
        _confirmPassword.value = confirmPassword
    }

    fun changePassword(context: Context, phoneNumber: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if(newPassword.value == confirmPassword.value){
                val user = repository.getUserByPhoneNumber(phoneNumber)
                user?.let {
                    val result = repository.setNewPassword(user.id, newPassword.value)
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                    /*Navigate to main screen*/
                    _uiState.value = UiState.Success
                }?: {
                    warning = "wrong"
                    _uiState.value = UiState.Error("user not found")
                }
            }else{
                warning = "password"
                _uiState.value = UiState.Success
            }
        }
    }
}