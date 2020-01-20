package com.example.delieverydemo.delievery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delievery.datasource.NetDeliveryDataSouce
import com.example.delieverydemo.delievery.datasource.factory.NetDeliveryDataSourceFactory
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.storage.AppDatabase
import com.example.delieverydemo.storage.DeliveryBoundaryCallback
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE

/**
 *
​
 * Purpose – Repository
 *
 * Pagging refrence below:-
 * Refrence:- https://www.raywenderlich.com/6948-paging-library-for-android-with-kotlin-creating-infinite-lists#toc-anchor-004

​
 * @author ​Rishabh Gupta
​
 * Created on January 10, 2020
​
 * Modified on January 11, 2020
 *
 * */
class DeliveryRepository(
    val api: ApiService,
    val db: AppDatabase,
    val boundaryCallback: DeliveryBoundaryCallback
) {
    var networkState: LiveData<NetworkState> = MutableLiveData()
    val itemDataSourceFactory = NetDeliveryDataSourceFactory()


    /**
     * A PagedList is just a modified list. It integrates with a DataSource to provide content as
    items in the list get consumed. As whoever is accessing items on the PagedList begins to
    approach the bottom of the list, it will delegate off to its DataSource to fetch new items.
     */
    fun getPagedList(): LiveData<PagedList<DeliveryResponseModel>> {
        val dataSourceFactory = db.getDeliveryDao().getAllDelivery()

        //controls how many items the DataSource will attempt to fetch
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(LOADING_PAGE_SIZE)
            .build()


        /**
         * This method takes in the config you defined above and DataSource(Here db.getDeliveryDao().getAllDelivery()),
         * and returns LivePagedListBuilder to build the LiveData.
        You’re observing the same LiveData and submitting the value it emits via the submitList method(in fragment/Activity).
         */
        val livePageListBuilder =
            LivePagedListBuilder<Int, DeliveryResponseModel>(
                dataSourceFactory,
                pagedListConfig
            )
                .setBoundaryCallback(boundaryCallback)
                .build()

        return livePageListBuilder
    }

    fun getPageLoadingState(): MutableLiveData<NetworkState> {
        networkState = Transformations.switchMap(
            itemDataSourceFactory.liveNotificationDataSource,
            NetDeliveryDataSouce::networkState
        )

        return networkState as MutableLiveData<NetworkState>
    }

    /**
     *  The LiveData will simply emit a new copy of PagedList that you’ll feed
     *  into the adapter, and everything will automagically work.
     */
    fun stopLoadingAndRefresh() {
        itemDataSourceFactory.itemLiveDataSource.value?.invalidate()
    }


}
