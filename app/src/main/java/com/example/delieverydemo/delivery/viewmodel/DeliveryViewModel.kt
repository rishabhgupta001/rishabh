package com.example.delieverydemo.delivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.delieverydemo.api.NetworkState
import com.example.delieverydemo.delivery.DeliveryRepository
import com.example.delieverydemo.delivery.model.DeliveryResponseModel


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

    //fun getNetworkState(): MutableLiveData<NetworkState> = repository.getPageLoadingState()
    fun getNetworkState(): MutableLiveData<NetworkState> = repository.getPageLoadingState2()

    /**
     * Method to Signal the data source to stop loading, and notify its callback
     */
   // fun onSwipeRefrenh() = repository.stopLoadingAndRefresh()
    fun onSwipeRefrenh() = repository.stopLoadingAndRefresh2()


    /**
     * Method add favourite item of Delivery List  into db
     *
     * Specific method for DeliveryDetailFragment
     */
    fun setFav(itemData: DeliveryResponseModel) {
        repository.setFavInDb(itemData)
    }


}
