package com.example.delieverydemo.delievery.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delievery.datasource.DeliveryDataSouce
import com.example.delieverydemo.delievery.datasource.factory.DeliveryDataSourceFactory
import com.example.delieverydemo.delievery.model.DeliveryResponseModel

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    var itemPagedList: LiveData<PagedList<DeliveryResponseModel>> = MutableLiveData<PagedList<DeliveryResponseModel>>()
    var liveDataSource: LiveData<PageKeyedDataSource<Int, DeliveryResponseModel>> = MutableLiveData<PageKeyedDataSource<Int, DeliveryResponseModel>>()
    var networkState: LiveData<NetworkState> = MutableLiveData()
    val itemDataSourceFactory =
        DeliveryDataSourceFactory()

   /* val deliveryResponseData: MutableLiveData<ArrayList<DeliveryResponseModel>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var statusListener: StatusListener? = null*/

    init {
        liveDataSource = itemDataSourceFactory.itemLiveDataSource

        networkState = Transformations.switchMap(itemDataSourceFactory.liveNotificationDataSource, DeliveryDataSouce::networkState)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(10).build()

        itemPagedList = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()
    }

   /* //Hitting Login Api
    @SuppressLint("CheckResult")
    fun getDeliveryList() {
        val responseModel = ArrayList<DeliveryResponseModel>()
        if (!Utils.isNetworkAvailable(getApplication())) {
            //responseModel.status = StatusCode.NETWORK_ERROR
            statusListener?.onFailure(MyApplication.mInstance.getString(R.string.text_no_internet_available))
            deliveryResponseData.value = responseModel
        } else {


            val observable = ApiService.create().getDeliveryList(20, 20)
            compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        statusListener?.onStarted()
                        deliveryResponseData.setValue(responseModel)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ success ->
                        statusListener?.onSuccess(Any())
                        deliveryResponseData.setValue(success)
                    }, { e ->
                        e.printStackTrace()
                        Log.e("vvv1", "Error ${e.localizedMessage}")
                        Log.e("vvv2", "Error ${e.message}")
                        statusListener?.onFailure(e?.localizedMessage!!)
                        deliveryResponseData.setValue(responseModel)
                    }, {})
            )
        }
    }*/

  /*  //This method will be called when this ViewModel is no longer used and will be destroyed.
    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }*/

}