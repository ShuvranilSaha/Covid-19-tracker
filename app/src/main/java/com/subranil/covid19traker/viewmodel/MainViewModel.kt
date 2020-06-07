package com.subranil.covid19traker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.subranil.covid19traker.models.StateResponse
import com.subranil.covid19traker.repository.CovidIndiaRepository
import com.subranil.covid19traker.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val data: CovidIndiaRepository) : ViewModel() {
    private val _liveData = MutableLiveData<State<StateResponse>>()

    val liveData: LiveData<State<StateResponse>> = _liveData
    //todo

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun getData() {
        viewModelScope.launch {
            data.getData().collect {
                _liveData.value = it
            }
        }
    }
}