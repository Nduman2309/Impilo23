package com.example.impilo23.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Professional network builder utilizing standard public endpoints.
 */
object RetrofitClient {
    private const val BASE_URL = "https://disease.sh/v3/covid-19/"

    val instance: HealthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(HealthApiService::class.java)
    }
}
