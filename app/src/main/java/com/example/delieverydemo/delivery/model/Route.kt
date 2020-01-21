package com.example.delieverydemo.delivery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Route(
    val end: String = "",
    val start: String = ""
) : Parcelable