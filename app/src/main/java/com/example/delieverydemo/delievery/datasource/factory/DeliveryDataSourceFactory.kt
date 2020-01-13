package com.example.delieverydemo.delievery.datasource.factory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.DataSource.Factory
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.application.MyApplication
import com.example.delieverydemo.delievery.datasource.DeliveryDataSouce
import com.example.delieverydemo.delievery.model.DeliveryResponseModel

class DeliveryDataSourceFactory : Factory<Int, DeliveryResponseModel>() {
    val application: Application = MyApplication.mInstance
    //creating the mutable live data
    //getter for itemlivedatasource
    val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, DeliveryResponseModel>>()

    var liveNotificationDataSource = MutableLiveData<DeliveryDataSouce>()

    init {
        //  liveNotificationDataSource = MutableLiveData<NotificationDataSource>()
    }


    override fun create(): DataSource<Int, DeliveryResponseModel> {
        //getting our data source object
        val itemDataSource =
            DeliveryDataSouce(
                application
            )

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSource)
        liveNotificationDataSource.postValue(itemDataSource)

        //returning the datasource
        return itemDataSource
    }
}