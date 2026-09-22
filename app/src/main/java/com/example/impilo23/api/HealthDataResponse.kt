package com.example.impilo23.api

import com.google.gson.annotations.SerializedName

/**
 * Model class for public health statistical metrics mapping the REST API fields safely.
 */
data class HealthDataResponse(
    @SerializedName("country") val countryName: String?,
    @SerializedName("cases") val totalCases: Long,
    @SerializedName("todayCases") val todayCases: Long,
    @SerializedName("deaths") val totalDeaths: Long,
    @SerializedName("todayDeaths") val todayDeaths: Long,
    @SerializedName("recovered") val totalRecovered: Long,
    @SerializedName("active") val activeCases: Long,
    @SerializedName("critical") val criticalCases: Long,
    @SerializedName("population") val populationSize: Long
)
