package com.subranil.covid19traker

import android.app.Application
import com.subranil.covid19traker.di.networkModule
import com.subranil.covid19traker.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CovidTrackerApp: Application() {

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        initialise()
    }
    @ExperimentalCoroutinesApi
    private fun initialise() {
        startKoin {
            androidContext(applicationContext)
            modules(networkModule, viewModelModule)
        }
    }
}