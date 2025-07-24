package com.example.diseasedetector.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val base_url = "https://diseasedetectorapi-chatbot-9486105925.us-central1.run.app"

    val instance: ChatApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChatApi::class.java)
    }
}