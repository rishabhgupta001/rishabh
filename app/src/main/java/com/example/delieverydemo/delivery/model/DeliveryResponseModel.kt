package com.example.delieverydemo.delivery.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.delieverydemo.utils.Constants.CURRENCY
import com.example.delieverydemo.utils.Constants.DECIMAL_PATTERN
import kotlinx.android.parcel.Parcelize
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Immutable model class for a delivery and entity in the Room database.
 */

@Parcelize
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
) : Parcelable {

    val price: String
        get() {
            //to take value upto 2 decimal digit (ex:- x.xx)
            val df = DecimalFormat(DECIMAL_PATTERN)
            df.roundingMode = RoundingMode.CEILING

            //97.67 (looks like this)              Below adding deliveryFee and surcharge
            val amount = df.format(
                (deliveryFee.removePrefix(CURRENCY).toFloat()) + (surcharge.removePrefix(CURRENCY).toFloat())
            )

            //https://www.baeldung.com/kotlin-concatenate-strings  for concatenation
            return CURRENCY.plus(amount)
        }

}