package com.example.delieverydemo.delivery

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delivery.datasource.NetDeliveryDataSouce
import com.example.delieverydemo.delivery.datasource.factory.NetDeliveryDataSourceFactory
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.storage.AppDatabase
import com.example.delieverydemo.storage.DeliveryBoundaryCallback
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

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
    private val api: ApiService,
    private val db: AppDatabase,
    private val boundaryCallback: DeliveryBoundaryCallback
) {
    private var networkState: LiveData<NetworkState> = MutableLiveData()
    private val itemDataSourceFactory = NetDeliveryDataSourceFactory()
    private val liveBoundryCallback = MutableLiveData<DeliveryBoundaryCallback>()
    private val itemLiveDataSource = MutableLiveData<PagedList.BoundaryCallback<DeliveryResponseModel>>()
    private val dataSourceDao = MutableLiveData<DataSource.Factory<Int, DeliveryResponseModel>>()

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

        liveBoundryCallback.postValue(boundaryCallback)
        dataSourceDao.postValue(dataSourceFactory)

        return livePageListBuilder

        /*----------------------*/

        /*val livePageListBuilder = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()

        return livePageListBuilder*/
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

    /**
     * Method add favourite item of Delivery List into db
     *
     * Specific method for DeliveryDetailFragment
     */
    @SuppressLint("CheckResult")
    fun setFavInDb(itemData: DeliveryResponseModel) {
        Completable.complete().subscribeOn(Schedulers.io()).subscribe {
            //updating db item on another thread
            db.getDeliveryDao().updateItem(itemData)
        }
    }

    fun getPageLoadingState2(): MutableLiveData<NetworkState> {
        networkState = Transformations.switchMap(
            liveBoundryCallback,
            DeliveryBoundaryCallback::networkState
        )
        return networkState as MutableLiveData<NetworkState>
    }

    fun stopLoadingAndRefresh2() {
        (dataSourceDao as DataSource<Int, DeliveryResponseModel>).invalidate()
    }

}

/*class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
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
}*/
