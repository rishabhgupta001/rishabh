package com.example.delieverydemo.data.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagedList
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.preference.NEXT_OFFSET_COUNT
import com.example.delieverydemo.data.preference.Pref
import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.AppExecutor
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import com.example.delieverydemo.utils.PagingRequestHelper
import com.example.delieverydemo.utils.createStatusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * PagedList :- A PagedList is a version of a List that loads content in chunks.
 * When a PagedList is created, it immediately loads the first chunk of data and expands over time
 * as content is loaded in future passes. The size of PagedList is the number of items loaded
 * during each pass. The class supports both infinite lists and very large lists with a fixed
 * number of elements.
 *
 *  create a boundary callback which will observe when the user reaches to the edges of
 *  the list and update the database with extra data.
 */
class DeliveryBoundaryCallback(
    private val apiService: ApiService,
    private val pref: Pref,
    private val executor: AppExecutor,
    private val handleResponse: (List<DeliveryResponseModel>) -> Unit
) : PagedList.BoundaryCallback<DeliveryResponseModel>() {

    private val TAG = DeliveryBoundaryCallback::class.java.simpleName
    private var disposable = CompositeDisposable()
    //to determine what thread to use when running the network and database tasks.
    val helper = PagingRequestHelper(executor.diskIo)
    val networkState = helper.createStatusLiveData()
    private var isNotLoaded: Boolean = true


    /**
     * Method lets you know when it has no data
     *
     * initally/First, load data into the Room database via the API.
     */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Log.d(TAG, "onZeroItemsLoaded called")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
            fetchDelivery(helperCallback, 0)
        }
    }

    /**
     * Method lets you know when the item at the end of its available data has been loaded
     */
    override fun onItemAtEndLoaded(itemAtEnd: DeliveryResponseModel) {
        super.onItemAtEndLoaded(itemAtEnd)
        Log.d(TAG, "onItemAtEndLoaded called")
        Log.d(
            TAG, "onItemAtEndLoaded total count ${pref.getInt(
                NEXT_OFFSET_COUNT
            )}"
        )
        if (isNotLoaded) {
            Log.d(TAG, "onItemAtEndLoaded total isNotLoaded $isNotLoaded")
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
                fetchDelivery(helperCallback, pref.getInt(NEXT_OFFSET_COUNT))
            }
        }
    }

    private fun fetchDelivery(
        helperCallback: PagingRequestHelper.Request.Callback,
        offset: Int
    ) {
        val observable = apiService.getDeliveryList(LOADING_PAGE_SIZE, offset)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .doOnSubscribe {
                }
                .observeOn(AndroidSchedulers.mainThread())
                //below code is working on Main thread
                .subscribe(
                    { sucess ->
                        success(helperCallback, sucess)
                    }, { error ->
                        handleError(helperCallback, error)
                    })
        )

    }

    @SuppressLint("CheckResult")
    private fun success(
        helperCallback: PagingRequestHelper.Request.Callback,
        data: List<DeliveryResponseModel>
    ) {
        if (data.size > 0) {
            Log.d(TAG, "fetchDelivery success loading")
            pref.setInt(NEXT_OFFSET_COUNT, pref.getInt(NEXT_OFFSET_COUNT) + data.size)

            /*Completable.complete().subscribeOn(Schedulers.io())
                //currently below code is running on Schedulers.io() thread
                .subscribe { db.getDeliveryDao().insertAll(data) }*/

            /* executor.diskIo.execute {
                 db.getDeliveryDao().insertAll(data)
                 helperCallback.recordSuccess()
             }*/

            executor.diskIo.execute {
                handleResponse(data)
                helperCallback.recordSuccess()
            }
        } else {
            isNotLoaded = false
            Log.d(TAG, "fetchDelivery success No data found")
        }
    }

    private fun handleError(
        helperCallback: PagingRequestHelper.Request.Callback,
        throwable: Throwable
    ) {
        helperCallback.recordFailure(throwable)
        Log.d(TAG, "fetch delivery Error  ${throwable.message}")
    }

}