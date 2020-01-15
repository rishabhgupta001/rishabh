package com.example.delieverydemo.storage.paging

import androidx.paging.DataSource
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.storage.DeliveryDao

class DbDeliveryDataSourceFactory(val dao: DeliveryDao) :
    DataSource.Factory<Int, DeliveryResponseModel>() {

    private lateinit var deliveryPageKeyedDataSource: DbMoviesPageKeyedDataSource

    init {
        deliveryPageKeyedDataSource =
            DbMoviesPageKeyedDataSource(
                dao
            )
    }

    override fun create(): DataSource<Int, DeliveryResponseModel> =
        deliveryPageKeyedDataSource
}
