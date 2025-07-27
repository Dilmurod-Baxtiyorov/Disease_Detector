package com.example.diseasedetector.data.api

import com.example.diseasedetector.data.model.ChatRequest
import com.example.diseasedetector.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    //https://diseasedetectorapi-chatbot-9486105925.us-central1.run.app/chat
    @POST("/chat")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}