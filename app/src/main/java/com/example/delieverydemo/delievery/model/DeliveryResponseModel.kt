package com.example.delieverydemo.delievery.model

import com.example.delieverydemo.api.StatusCode

data class DeliveryResponseModel(
    val deliveryFee: String = "",
    val goodsPicture: String = "",
    val id: String = "",
    val pickupTime: String = "",
    val remarks: String = "",
    val route: Route? = null,
    val sender: Sender? = null,
    val surcharge: String = "",
    var status: StatusCode = StatusCode.START,
    var error: String = ""
)