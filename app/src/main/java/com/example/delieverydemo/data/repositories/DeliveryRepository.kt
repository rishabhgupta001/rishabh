package com.example.delieverydemo.data.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.delieverydemo.data.db.AppDatabase
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.preference.NEXT_OFFSET_COUNT
import com.example.delieverydemo.data.preference.Pref
import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.ui.delivery.model.ListingDataModel
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
    private val executor: AppExecutor,
    private val pref: Pref
) : DeliveryPostRepository {
    private lateinit var boundaryCallback: DeliveryBoundaryCallback
    private val TAG = DeliveryRepository::class.java.simpleName

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

    override fun postOfDelivery(): ListingDataModel<DeliveryResponseModel> {
        boundaryCallback =
            DeliveryBoundaryCallback(
                api,
                pref,
                executor,
                ::insertDataIntoDb
            )

        /**
         * A PagedList is just a modified list. It integrates with a DataSource to provide content as
        items in the list get consumed. As whoever is accessing items on the PagedList begins to
        approach the bottom of the list, it will delegate off to its DataSource to fetch new items.
         */
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
                .setBoundaryCallback(boundaryCallback)
                .build()


        return ListingDataModel(
            pagedList = livePageList,
            networkState = boundaryCallback.networkState,
            refreshState = refreshState,
            refresh = { refreshTrigger.value = null },
            retry = { boundaryCallback.helper.retryAllFailed() }
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
                        // db.getDeliveryDao().insertAll(success)
                        if (success.size > 0) {
                            pref.setInt(NEXT_OFFSET_COUNT, success.size)
                            insertDataIntoDb(success)
                        } else {
                            Log.d(TAG, "fetchDelivery success No data found")
                        }
                    }
                    //since we are in bg thread now, post the result
                    networkState.postValue(NetworkState.SUCCESS)
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

    /**
     * Inserts the response into the database
     *
     * mapIndexed() Refrence:- https://www.geeksforgeeks.org/kotlin-collection-transformation/
     */
    private fun insertDataIntoDb(data: List<DeliveryResponseModel>) {
        db.runInTransaction {
            val start = db.getDeliveryDao().getNextIndexInDelivery()
            val items = data.mapIndexed { index, deliveryItem ->
                //Log.d("DEBUG", "Repo:-  start:-  $start  index:- $index")
                //for indexing rows of table
                deliveryItem.indexInResonse = start + index
                deliveryItem
            }
            db.getDeliveryDao().insertAll(items)
        }
    }
}
