package com.subranil.covid19traker.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StateResponse(
    @Json(name = "statewise")
    val stateWiseDetails: List<Details>
) : Parcelable