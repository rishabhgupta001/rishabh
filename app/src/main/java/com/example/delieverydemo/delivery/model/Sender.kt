package com.example.delieverydemo.delivery.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sender(
    val email: String = "",
    val name: String = "",
    val phone: String = ""
) : Parcelable