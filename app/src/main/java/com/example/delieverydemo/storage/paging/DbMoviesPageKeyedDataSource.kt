package com.example.delieverydemo.storage.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.storage.DeliveryDao

class DbMoviesPageKeyedDataSource(val dao: DeliveryDao) :
    PageKeyedDataSource<Int, DeliveryResponseModel>() {
    private val TAG: String = DbMoviesPageKeyedDataSource::class.java.getSimpleName()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DeliveryResponseModel>
    ) {
        Log.d(TAG, "Loading Initial Rang, Count ${params.requestedLoadSize}")
        val deliveryList = dao.getAllDelivery()
        if (deliveryList.size > 0)
            callback.onResult(deliveryList, null, 2)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
    }
}