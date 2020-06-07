package com.subranil.covid19traker.repository

import com.subranil.covid19traker.api.Covid19ApiService
import com.subranil.covid19traker.models.StateDetailsResponse
import com.subranil.covid19traker.models.StateResponse
import com.subranil.covid19traker.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response

@ExperimentalCoroutinesApi
@FlowPreview
class CovidIndiaRepository(private val apiService: Covid19ApiService) {

    fun getData(): Flow<State<StateResponse>> {
        return object : NetworkBoundRepository<StateResponse>() {
            override suspend fun fetchFromRemote(): Response<StateResponse> = apiService.getData()
        }.asFlow().flowOn(Dispatchers.IO)
    }

    fun getStateDetailsData(stateName: String): Flow<State<StateDetailsResponse>> {
        return object : NetworkBoundRepository<List<StateDetailsResponse>>() {
            override suspend fun fetchFromRemote(): Response<List<StateDetailsResponse>> =
                apiService.getStateDistrictData()
        }.asFlow().flowOn(Dispatchers.IO).map {
            when (it) {
                is State.Loading -> State.loading()
                is State.Success -> {
                    val data = it.data.find { it.state == stateName }

                    if (data != null) {
                        State.success<StateDetailsResponse>(data)
                    } else {
                        State.error<StateDetailsResponse>("No data found '$stateName'")
                    }
                }
                is State.Error -> {
                    State.error<StateDetailsResponse>(it.message)
                }
            }
        }
    }
}