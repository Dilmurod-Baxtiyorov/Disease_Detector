package com.example.diseasedetector.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseasedetector.data.repository.ChatRepository
import com.example.diseasedetector.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {
    private val chatRep = ChatRepository()

    private val _prompt = MutableStateFlow("")
    val prompt: StateFlow<String> = _prompt.asStateFlow()

    private var _history = mutableStateListOf<String>()
    val history: MutableList<String> get() = _history

    var warning = mutableStateOf("")
        private set

    fun onPromptChange(newPrompt: String){
        _prompt.value = newPrompt
    }

    fun onHistoryChange(text: String, author: String){
        if (author == "user" && text.isNotEmpty()){
            _history.add("user:$text")
        }else{
            history.add(text)
        }
    }

    fun sendRequest(){
        viewModelScope.launch {
            history.add("")
            val response = chatRep.sendRequest(history[history.size-2])
            if(response.isNotEmpty()){
                if(response.startsWith("chat:")){
                    history[history.size-1] = response.substring(5)
                }else{
                    history[history.size-1] = response
                }
            } else{
                history[history.size-1] = "Error: It seems like servers aren't working. Try again"
            }
        }
    }
}