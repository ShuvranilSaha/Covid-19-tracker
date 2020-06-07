package com.subranil.covid19traker.repository

import androidx.annotation.MainThread
import com.subranil.covid19traker.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<T> {
    fun asFlow() = flow<State<T>> {
        emit(State.loading())

        try {
            // fetch data
            val apiResponse = fetchFromRemote()

            // parse the data
            val remotePosts = apiResponse.body()

            if (apiResponse.isSuccessful && remotePosts !== null) {
                emit(State.success(remotePosts))
            } else {
                emit(State.error(apiResponse.message()))
            }
        } catch (e: Exception) {
            emit(State.error("Network Error"))
            e.printStackTrace()
        }
    }

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<T>
}