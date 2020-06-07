package com.subranil.covid19traker.api

import com.subranil.covid19traker.models.StateDetailsResponse
import com.subranil.covid19traker.models.StateResponse
import retrofit2.Response
import retrofit2.http.GET

interface Covid19ApiService {

    companion object {
        const val BASE_URL = "https://api.covid19india.org/"
    }

    @GET("data.json")
    suspend fun getData(): Response<StateResponse>

    @GET("v2/state_district_wise.json")
    suspend fun getStateDistrictData(): Response<List<StateDetailsResponse>>

}