package com.subranil.covid19traker.di

import com.subranil.covid19traker.viewmodel.MainViewModel
import com.subranil.covid19traker.viewmodel.StateDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (MainViewModel(get())) }
    viewModel { (StateDetailsViewModel(get())) }
}