package com.example.delieverydemo.delievery.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delievery.DeliveryRepository
import com.example.delieverydemo.delievery.model.DeliveryResponseModel


/**
 *
​
 * Purpose – ViewModel
​
 * @author ​Rishabh Gupta
​
 * Created on January 10, 2020
​
 * Modified on January 11, 2020
 *
 * */

class DeliveryViewModel(val repository: DeliveryRepository) : ViewModel() {

    fun getDeliveryList(): LiveData<PagedList<DeliveryResponseModel>> = repository.getPagedList()

    fun getNetworkState(): MutableLiveData<NetworkState> = repository.getPageLoadingState()

    /**
     * Method to Signal the data source to stop loading, and notify its callback
     */
    fun onSwipeRefrenh() = repository.stopLoadingAndRefresh()


}
