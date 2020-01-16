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
import com.example.delieverydemo.delievery.datasource.NetDeliveryDataSouce
import com.example.delieverydemo.delievery.datasource.factory.NetDeliveryDataSourceFactory
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    var itemPagedList: LiveData<PagedList<DeliveryResponseModel>> = MutableLiveData<PagedList<DeliveryResponseModel>>()
    var liveDataSource: LiveData<PageKeyedDataSource<Int, DeliveryResponseModel>> = MutableLiveData<PageKeyedDataSource<Int, DeliveryResponseModel>>()
    var networkState: LiveData<NetworkState> = MutableLiveData()
    val itemDataSourceFactory =
        NetDeliveryDataSourceFactory()

    init {
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        networkState = Transformations.switchMap(itemDataSourceFactory.liveNotificationDataSource, NetDeliveryDataSouce::networkState)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(LOADING_PAGE_SIZE).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()
    }
}