package com.example.impilo23.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Endpoint definitions for fetching live disease metadata across countries.
 */
interface HealthApiService {
    @GET("countries/{country}")
    fun getCountryStats(@Path("country") country: String): Call<HealthDataResponse>
}
