package com.example.delieverydemo.delievery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delievery.datasource.DeliveryDataSouce
import com.example.delieverydemo.delievery.datasource.factory.DeliveryDataSourceFactory
import com.example.delieverydemo.delievery.model.DeliveryResponseModel

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    var itemPagedList: LiveData<PagedList<DeliveryResponseModel>> = MutableLiveData<PagedList<DeliveryResponseModel>>()
    var liveDataSource: LiveData<PageKeyedDataSource<Int, DeliveryResponseModel>> = MutableLiveData<PageKeyedDataSource<Int, DeliveryResponseModel>>()
    var networkState: LiveData<NetworkState> = MutableLiveData()
    val itemDataSourceFactory =
        DeliveryDataSourceFactory()

    init {
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        networkState = Transformations.switchMap(itemDataSourceFactory.liveNotificationDataSource, DeliveryDataSouce::networkState)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(5).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()
    }
}