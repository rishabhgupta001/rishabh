package com.example.delieverydemo.data.repositories

import com.example.delieverydemo.delivery.model.DeliveryResponseModel

/**
 *
​
 * Purpose – Making common interface that can be shared by the different repository implementations.
 *
 * @author ​Rishabh Gupta
​
 * Created on January 28, 2020
​
 * Modified on January 28, 2020
 *
 * */

interface DeliveryPostRepository {

    fun postOfDelivery(): Listing<DeliveryResponseModel>
}