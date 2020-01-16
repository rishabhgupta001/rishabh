package com.example.delieverydemo.delievery.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Immutable model class for a delivery and entity in the Room database.
 */

@Entity(tableName = "delivery")
data class DeliveryResponseModel(
    val deliveryFee: String = "",
    val goodsPicture: String = "",
    @PrimaryKey
    val id: String = "",
    val pickupTime: String = "",
    val remarks: String = "",
    val surcharge: String = "",
    var error: String = "",
    @Embedded
    val route: Route? = null,
    @Embedded
    val sender: Sender? = null
) : Serializable