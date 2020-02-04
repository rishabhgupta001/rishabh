package com.example.delieverydemo.data.db

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.network.StatusCode
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * PagedList :- A PagedList is a version of a List that loads content in chunks.
 * When a PagedList is created, it immediately loads the first chunk of data and expands over time
 * as content is loaded in future passes. The size of PagedList is the number of items loaded
 * during each pass. The class supports both infinite lists and very large lists with a fixed
 * number of elements.
 */
class DeliveryBoundaryCallback(private val apiService: ApiService, private val db: AppDatabase) :
    PagedList.BoundaryCallback<DeliveryResponseModel>() {

    private val TAG = DeliveryBoundaryCallback::class.java.simpleName
    private var totalCount: Int = 0
    private var isLoaded: Boolean = false
    var networkState: MutableLiveData<NetworkState> = MutableLiveData()
    private var disposable = CompositeDisposable()

    /**
     * Method lets you know when it has no data
     *
     * initally/First, load data into the Room database via the API.
     */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Log.d(TAG, "onZeroItemsLoaded called")
        fetchDelivery(LOADING_PAGE_SIZE, 0)
    }

    /**
     * Method lets you know when the item at the end of its available data has been loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: DeliveryResponseModel) {
        super.onItemAtEndLoaded(itemAtEnd)
        Log.d(TAG, "onItemAtEndLoaded called")
        Log.d(TAG, "onItemAtEndLoaded total count $totalCount")
        if (!isLoaded) {
            Log.d(TAG, "onItemAtEndLoaded total isloaded $isLoaded")
            fetchDelivery(LOADING_PAGE_SIZE, totalCount)
        }
    }


    @SuppressLint("CheckResult")
    fun fetchDelivery(limit: Int, offset: Int) {
        Log.d(TAG, "limit:- $limit   offset:- $offset")
        if (offset == 0)
            Log.d(TAG, "fetchDelivery called first Time")
        else
            Log.d(TAG, "fetchDelivery called second Time")

        val observable = apiService.getDeliveryList(limit, offset)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .doOnSubscribe {
                }
                .observeOn(AndroidSchedulers.mainThread())
                //below code is working on Main thread
                .subscribe(
                    { sucess ->
                        success(sucess)
                    }, { error ->
                        handleError(error)
                    })
        )
    }

    @SuppressLint("CheckResult")
    private fun success(data: ArrayList<DeliveryResponseModel>) {
        updateState(NetworkState.SUCCESS)
        if (data.size > 0) {
            Log.d(TAG, "fetchDelivery success loading")
            totalCount = data.size
            if (data.size < LOADING_PAGE_SIZE) {
                isLoaded = true
                Log.d(TAG, "fetchDelivery success item less than LOADING_PAGE_SIZE")

            } else {
                Log.d(TAG, "fetchDelivery success item more than than LOADING_PAGE_SIZE")
            }

            //
            Completable.complete().subscribeOn(Schedulers.io())
                //currently below code is running on Schedulers.io() thread
                .subscribe { db.getDeliveryDao().insertAll(data) }
        } else {
            isLoaded = true
            Log.d(TAG, "fetchDelivery success No data found")
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.d(TAG, "fetch delivery Error  ${throwable.message}")
        updateState(NetworkState(StatusCode.ERROR, throwable.message!!))
    }

    fun clear() {
        disposable.clear()
    }

    fun retry() {
        fetchDelivery(LOADING_PAGE_SIZE, totalCount)
    }

    fun onRefresh() {
        totalCount = 0
        isLoaded = false
        disposable.clear()
        onZeroItemsLoaded()
    }

    private fun updateState(networkState: NetworkState) {
        this.networkState.postValue(networkState)
    }
}