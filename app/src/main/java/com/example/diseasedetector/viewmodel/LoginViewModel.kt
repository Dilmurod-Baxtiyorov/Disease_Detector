package com.example.diseasedetector.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.navigation.Routes
import com.example.diseasedetector.data.repository.AuthRepository
import com.example.diseasedetector.data.repository.DataManager
import com.example.diseasedetector.ui.state.UiEvent
import com.example.diseasedetector.ui.state.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dataManager: DataManager,
    auth: FirebaseAuth,
    db: FirebaseFirestore
): ViewModel() {
    private val repository = AuthRepository(auth, db)

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> get() = _uiState

    private val _event = MutableSharedFlow<UiEvent>()
    val  event = _event.asSharedFlow()

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
        _password.value = newPassword
    }

    fun checkIdentifier(): Boolean{
        return !(identifier.value == "").also { warning = if(it) "empty" else "" }
    }

    fun checkPassword(): Boolean{
        return !(identifier.value == "").also { warning = if(it) "blank" else ""}
    }

    fun checkPhoneNumber(): Boolean{
        if(!identifier.value.matches(Regex("^\\+998\\d{9}$"))){
            warning = "phone"
            return false
        }
        return true
    }

    fun login(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val user = repository.getUserByIdentifier(identifier = identifier.value)
            if(user != null){
                val firebaseUser = repository.signInWithEmail(email = identifier.value, password = password.value)
                if(firebaseUser != null){
                    dataManager.saveUser(user.fullName)
                    _event.emit(UiEvent.Navigate(Routes.Main.name))
                }else{
                    warning = "password"
                    _uiState.value = UiState.Success
                }
            }else{
                warning = "wrong"
                _uiState.value = UiState.Idle
            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.resetEmailPassword(email)
            warning = "email"
            _uiState.value = UiState.Idle
        }
    }
}