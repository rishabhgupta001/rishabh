package com.example.delieverydemo.delievery.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.delieverydemo.utils.Constants.CURRENCY
import com.example.delieverydemo.utils.Constants.DECIMAL_PATTERN
import java.io.Serializable
import java.math.RoundingMode
import java.text.DecimalFormat

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
    val sender: Sender? = null,
    //not comoing from Api
    var isFavourite: Boolean = false
) : Serializable {

    val price: String
        get() {
            val dfee = deliveryFee.removePrefix(CURRENCY).toFloat()
            val surcharge = surcharge.removePrefix(CURRENCY).toFloat()

            val df = DecimalFormat(DECIMAL_PATTERN)
            df.roundingMode = RoundingMode.CEILING

            return df.format(dfee + surcharge)
        }

}