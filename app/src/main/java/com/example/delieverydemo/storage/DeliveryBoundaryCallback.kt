package com.example.delieverydemo.storage

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.api.StatusCode
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeliveryBoundaryCallback(val apiService: ApiService, val db: AppDatabase) :
    PagedList.BoundaryCallback<DeliveryResponseModel>() {

    private val TAG = DeliveryBoundaryCallback::class.java.simpleName
    var totalCount: Int = 0
    private var isLoaded: Boolean = false
    var networkState: MutableLiveData<NetworkState> = MutableLiveData()
    var disposable = CompositeDisposable()

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
        if (!isLoaded)
            fetchDelivery(LOADING_PAGE_SIZE, totalCount)
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
                        updateState(NetworkState.SUCCESS)
                        success(sucess)
                    }, { error ->
                        updateState(NetworkState(StatusCode.ERROR, error.message!!))
                        handleError(error)
                    })
        )
    }

    @SuppressLint("CheckResult")
    private fun success(data: ArrayList<DeliveryResponseModel>) {
        if (data.size > 0) {
            Log.d(TAG, "fetchDelivery success loading")
            totalCount = data.size
            if (data.size < LOADING_PAGE_SIZE) {
                Log.d(TAG, "fetchDelivery success item less than LOADING_PAGE_SIZE")

            } else {
                Log.d(TAG, "fetchDelivery success item morethan than LOADING_PAGE_SIZE")
            }

            //
            Completable.complete().subscribeOn(Schedulers.io())
                //currently below code is running on Schedulers.io() thread
                .subscribe { db.getDeliveryDao().insertDelivery(data) }
        } else {
            Log.d(TAG, "fetchDelivery success No data found")
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.d(TAG, "fetch delivery Error  ${throwable.message}")
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

    fun updateState(networkState: NetworkState) {
        this.networkState.postValue(networkState)
    }
}