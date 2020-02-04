package com.example.delieverydemo.data.repositories

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.delieverydemo.data.db.AppDatabase
import com.example.delieverydemo.data.db.DeliveryBoundaryCallback2
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.preference.NEXT_OFFSET_COUNT
import com.example.delieverydemo.data.preference.Pref
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.AppExecutor
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
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
    private val boundaryCallback2: DeliveryBoundaryCallback2,
    private val executor: AppExecutor,
    private val pref: Pref
) : DeliveryPostRepository {
    private val liveBoundryCallback2 = MutableLiveData<DeliveryBoundaryCallback2>()
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
                .setBoundaryCallback(boundaryCallback2)
                .build()

        liveBoundryCallback2.postValue(boundaryCallback2)
        dataSourceDao.postValue(dataSourceFactory)

        return livePageListBuilder

        /*----------------------*/

        /*val livePageListBuilder = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()

        return livePageListBuilder*/
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

    override fun postOfDelivery(): Listing<DeliveryResponseModel> {
        //controls how many items the DataSource will attempt to fetch
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(LOADING_PAGE_SIZE)
            .build()


        val refreshTrigger = MutableLiveData<Any>()
        val refreshState = Transformations.switchMap(refreshTrigger, { _ -> refresh() }
        )


        /**
         * This method takes in the config you defined above and DataSource(Here db.getDeliveryDao().getAllDelivery()),
         * and returns LivePagedListBuilder to build the LiveData.
        You’re observing the same LiveData and submitting the value it emits via the submitList method(in fragment/Activity).
         */
        val livePageList =
            LivePagedListBuilder<Int, DeliveryResponseModel>(
                db.getDeliveryDao().getAllDelivery(),
                pagedListConfig
            )
                .setBoundaryCallback(boundaryCallback2)
                .build()


        return Listing(
            pagedList = livePageList,
            networkState = boundaryCallback2.networkState,
            refreshState = refreshState,
            refresh = { refreshTrigger.value = null },
            retry = { boundaryCallback2.helper.retryAllFailed() }
        )
    }


    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     *
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    @SuppressLint("CheckResult")
    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.START
        val observable = api.getDeliveryList(LOADING_PAGE_SIZE, 0)
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                executor.diskIo.execute {
                    db.runInTransaction {
                        db.getDeliveryDao().deleteAllMovies()
                        db.getDeliveryDao().insertAll(success)
                    }
                    //since we are in bg thread now, post the result
                    networkState.postValue(NetworkState.SUCCESS)
                    pref.setInt(NEXT_OFFSET_COUNT, 0)
                }
            },
                { error ->
                    //calls this on main thread so safe to call set value
                    networkState.value =
                        NetworkState.error(error.message!!)
                },
                {})
        return networkState
    }
}
