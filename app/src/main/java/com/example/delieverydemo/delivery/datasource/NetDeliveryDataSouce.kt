package com.example.delieverydemo.delivery.datasource

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.application.MyApplication
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.network.StatusCode
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

/**
 *
​
 * Purpose – Responsible for loading the data by page
​
 * @author ​Rishabh Gupta
​
 * Created on January 10, 2020
​
 * Modified on January 11, 2020
 *
 * */

class NetDeliveryDataSouce :
    PageKeyedDataSource<Int, DeliveryResponseModel>(), KodeinAware {

    override val kodein: Kodein by kodein(MyApplication.mInstance)
    private val apiService: ApiService by instance()

    private var limit: Int = 20
    var networkState: MutableLiveData<NetworkState>


    init {
        //initializing networkState live date to get network status while hitting api like: success, error etc.
        networkState = MutableLiveData()
    }

    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DeliveryResponseModel>
    ) {
        val observable = apiService.getDeliveryList(limit, 20)
        observable.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                networkState.postValue(NetworkState.START)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                networkState.postValue(NetworkState.SUCCESS)
                callback.onResult(success, null, 2)
            }, { error ->
                networkState.postValue(NetworkState(StatusCode.ERROR, error.message!!))
            }, {})
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
        val observable = apiService.getDeliveryList(limit, params.key)
        observable.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                networkState.postValue(NetworkState.START)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                networkState.postValue(NetworkState.SUCCESS)
                val adjacentKey = if (success?.size!! > 0) params.key + 1 else null
                if (!success.isNullOrEmpty()) {
                    callback.onResult(success, adjacentKey)
                }
            }, { error ->
                networkState.postValue(NetworkState(StatusCode.ERROR, error.message!!))
            }, {})
    }

    @SuppressLint("CheckResult")
    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
        val observable = apiService.getDeliveryList(limit, params.key)
        observable.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                networkState.postValue(NetworkState.START)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ success ->
                val adjacentKey = if (params.key > 1) params.key - 1 else null
                if (success != null) {
                    callback.onResult(success, adjacentKey)
                }
            }, { error ->
                networkState.postValue(NetworkState(StatusCode.ERROR, error.message!!))
            }, {})

    }
}

/*package com.example.delieverydemo.delievery.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.data.api.ApiService
import com.example.delieverydemo.data.api.NetworkState
import com.example.delieverydemo.data.api.StatusCode
import com.example.delieverydemo.application.MyApplication
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
​
 * Purpose – Responsible for loading the data by page
​
 * @author ​Rishabh Gupta
​
 * Created on January 10, 2020
​
 * Modified on January 11, 2020
 *
 * */

class NetDeliveryDataSouce :
    PageKeyedDataSource<Int, DeliveryResponseModel>(), KodeinAware {

    override val kodein: Kodein by kodein(MyApplication.mInstance)
    private val apiService: ApiService by instance()

    var limit: Int = 20
    var networkState: MutableLiveData<NetworkState>


    init {
        //initializing networkState live date to get network status while hitting api like: success, error etc.
        networkState = MutableLiveData<NetworkState>()
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DeliveryResponseModel>
    ) {
        networkState.postValue(NetworkState.START)

        apiService.getDeliveryList(limit, 20)
            .enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DeliveryResponseModel>>,
                    response: Response<ArrayList<DeliveryResponseModel>>
                ) {
                    if (response.isSuccessful) {
                        networkState.postValue(NetworkState.SUCCESS)
                        callback.onResult(response.body()!!, null, 2)
                    } else {
                        networkState.postValue(NetworkState(StatusCode.ERROR, response.message()))
                    }
                }

                override fun onFailure(call: Call<ArrayList<DeliveryResponseModel>>, t: Throwable) {
                    networkState.postValue(NetworkState(StatusCode.ERROR, t.message!!))
                }
            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
        networkState.postValue(NetworkState.START)

        apiService.getDeliveryList(limit, params.key)
            .enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DeliveryResponseModel>>,
                    response: Response<ArrayList<DeliveryResponseModel>>
                ) {
                    if (response.isSuccessful) {
                        networkState.postValue(NetworkState.SUCCESS)
                        val adjacentKey = if (response.body()?.size!! > 0) params.key + 1 else null
                        if (response.body() != null) {
                            callback.onResult(response.body()!!, adjacentKey)
                        }
                    } else {
                        loadAfter(params, callback)
                    }
                }

                override fun onFailure(call: Call<ArrayList<DeliveryResponseModel>>, t: Throwable) {
                    networkState.postValue(NetworkState(StatusCode.ERROR, t.message!!))
                }

            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, DeliveryResponseModel>
    ) {
        apiService.getDeliveryList(limit, params.key)
            .enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
                override fun onResponse(
                    call: Call<ArrayList<DeliveryResponseModel>>,
                    response: Response<ArrayList<DeliveryResponseModel>>
                ) {
                    val adjacentKey = if (params.key > 1) params.key - 1 else null
                    if (response.body() != null) {
                        callback.onResult(response.body()!!, adjacentKey)
                    }
                }

                override fun onFailure(call: Call<ArrayList<DeliveryResponseModel>>, t: Throwable) {
                    networkState.postValue(NetworkState(StatusCode.ERROR, t.message!!))
                }

            })
    }
}*/