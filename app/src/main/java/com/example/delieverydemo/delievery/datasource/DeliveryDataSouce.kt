package com.example.delieverydemo.delievery.datasource

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.api.StatusCode
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryDataSouce(application: Application) :
    PageKeyedDataSource<Int, DeliveryResponseModel>() {
    var limit: Int = 20
    var application: Application = application;
    val apiService = ApiService.create()
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

        apiService.getDeliveryList(limit, 20).enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
            override fun onResponse(call: Call<ArrayList<DeliveryResponseModel>>, response: Response<ArrayList<DeliveryResponseModel>>) {
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

        apiService.getDeliveryList(limit, params.key).enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
            override fun onResponse(call: Call<ArrayList<DeliveryResponseModel>>, response: Response<ArrayList<DeliveryResponseModel>>) {
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
        apiService.getDeliveryList(limit, params.key).enqueue(object : Callback<ArrayList<DeliveryResponseModel>> {
            override fun onResponse(call: Call<ArrayList<DeliveryResponseModel>>, response: Response<ArrayList<DeliveryResponseModel>>) {
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
}