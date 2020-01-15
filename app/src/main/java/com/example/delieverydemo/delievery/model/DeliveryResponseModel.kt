package com.example.delieverydemo.delievery.model

import androidx.room.Embedded
import androidx.room.Entity
import com.example.delieverydemo.api.StatusCode
import java.io.Serializable

/**
 * Immutable model class for a delivery and entity in the Room database.
 */

@Entity(tableName = "delivery")
data class DeliveryResponseModel(
    val deliveryFee: String = "",
    val goodsPicture: String = "",
    val id: String = "",
    val pickupTime: String = "",
    val remarks: String = "",
    val surcharge: String = "",
    var status: StatusCode = StatusCode.START,
    var error: String = "",
    @Embedded
    val route: Route? = null,
    @Embedded
    val sender: Sender? = null
) : Serializable