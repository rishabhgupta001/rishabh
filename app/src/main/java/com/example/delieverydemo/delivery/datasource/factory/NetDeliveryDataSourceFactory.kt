package com.example.delieverydemo.delivery.datasource.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.delivery.datasource.NetDeliveryDataSouce
import com.example.delieverydemo.delivery.model.DeliveryResponseModel

/**
 *
​
 * Purpose – Responsible for creating the DataSource so we can give it to the PagedList.
​
 * @author ​Rishabh Gupta
​
 * Created on January 10, 2020
​
 * Modified on January 11, 2020
 *
 * */

class NetDeliveryDataSourceFactory : Factory<Int, DeliveryResponseModel>() {
    //creating the mutable live data
    //getter for itemlivedatasource
    val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, DeliveryResponseModel>>()

    var liveNotificationDataSource = MutableLiveData<NetDeliveryDataSouce>()


    override fun create(): DataSource<Int, DeliveryResponseModel> {
        //getting our data source object
        val itemDataSource = NetDeliveryDataSouce()

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource)
        liveNotificationDataSource.postValue(itemDataSource)

        //returning the datasource
        return itemDataSource
    }
}