package com.subranil.covid19traker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subranil.covid19traker.models.StateDetailsResponse
import com.subranil.covid19traker.repository.CovidIndiaRepository
import com.subranil.covid19traker.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StateDetailsViewModel(private val data: CovidIndiaRepository): ViewModel() {
    private val _stateLiveData = MutableLiveData<State<StateDetailsResponse>>()

    val stateLiveData: LiveData<State<StateDetailsResponse>> = _stateLiveData

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getDistrictData(state: String) {
        viewModelScope.launch {
            data.getStateDetailsData(state).collect {
                _stateLiveData.value = it
            }
        }
    }
}