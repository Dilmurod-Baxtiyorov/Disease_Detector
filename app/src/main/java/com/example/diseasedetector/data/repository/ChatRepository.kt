package com.example.diseasedetector.data.repository

import com.example.diseasedetector.data.api.RetrofitInstance
import com.example.diseasedetector.data.model.ChatRequest

class ChatRepository {

    suspend fun sendRequest(prompt: String): String{
        return try {
            "chat:${RetrofitInstance.instance.sendMessage(ChatRequest(prompt)).response}"
        }catch (e: Exception){
            "Error:${e.message}"
        }
    }
}