package com.example.delieverydemo.delievery.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.delieverydemo.R
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.StatusCode
import com.example.delieverydemo.api.StatusListener
import com.example.delieverydemo.application.MyApplication
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeliveryViewModel(application: Application) : AndroidViewModel(application) {
    val deliveryResponseData: MutableLiveData<ArrayList<DeliveryResponseModel>> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var statusListener: StatusListener? = null




    //Hitting Login Api
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
    }

    //This method will be called when this ViewModel is no longer used and will be destroyed.
    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

}