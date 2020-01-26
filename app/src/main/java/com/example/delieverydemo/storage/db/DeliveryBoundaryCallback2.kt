package com.example.delieverydemo.storage.db

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagedList
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.LOADING_PAGE_SIZE
import com.example.delieverydemo.utils.PagingRequestHelper
import com.example.delieverydemo.utils.createStatusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

/**
 * PagedList :- A PagedList is a version of a List that loads content in chunks.
 * When a PagedList is created, it immediately loads the first chunk of data and expands over time
 * as content is loaded in future passes. The size of PagedList is the number of items loaded
 * during each pass. The class supports both infinite lists and very large lists with a fixed
 * number of elements.
 */
class DeliveryBoundaryCallback2(private val apiService: ApiService, private val db: AppDatabase) :
    PagedList.BoundaryCallback<DeliveryResponseModel>() {

    private val TAG = DeliveryBoundaryCallback2::class.java.simpleName
    private var disposable = CompositeDisposable()
    //to determine what thread to use when running the network and database tasks.
    private val executor = Executors.newSingleThreadExecutor()
    private val helper = PagingRequestHelper(executor)
    val networkState = helper.createStatusLiveData()
    private var totalCount: Int = 0
    private var isLoaded: Boolean = false


    /**
     * Method lets you know when it has no data
     *
     * initally/First, load data into the Room database via the API.
     */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        Log.d(TAG, "onZeroItemsLoaded called")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->

            val observable = apiService.getDeliveryList(LOADING_PAGE_SIZE, 0)
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
            helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
                val observable = apiService.getDeliveryList(LOADING_PAGE_SIZE, totalCount)
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

        }


    }

    @SuppressLint("CheckResult")
    private fun success(
        helperCallback: PagingRequestHelper.Request.Callback,
        data: ArrayList<DeliveryResponseModel>
    ) {
        if (data.size > 0) {
            Log.d(TAG, "fetchDelivery success loading")
           // Pref.setInt(app) = data.size
            if (data.size < LOADING_PAGE_SIZE) {
                isLoaded = true
                Log.d(TAG, "fetchDelivery success item less than LOADING_PAGE_SIZE")
            } else {
                Log.d(TAG, "fetchDelivery success item more than than LOADING_PAGE_SIZE")
            }

            /*Completable.complete().subscribeOn(Schedulers.io())
                //currently below code is running on Schedulers.io() thread
                .subscribe { db.getDeliveryDao().insertAll(data) }*/

            executor.execute {
                db.getDeliveryDao().insertAll(data)
                helperCallback.recordSuccess()
            }

        } else {
            isLoaded = true
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