package com.example.delieverydemo.ui.delivery.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.delieverydemo.data.repositories.DeliveryRepository
import com.example.delieverydemo.ui.delivery.model.DeliveryResponseModel


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

    val subDeliveryName = MutableLiveData<String>()
    //map:- everytime the value of subDelivery changes, repoResult will be updated too. (Here)
    val repoResult = Transformations.map(subDeliveryName, { _ -> repository.postOfDelivery() })

    /**
     * SwitchMap:- everytime the value of repoResult changes, it.pagedList will be called,
     * just like the map function. But it.pagedList returns a LiveData. So everytime that
     * the value of the LiveData returned by it.pagedList changes, the value of post
     * will change too. So the value of post will depend on changes of repoResult and changes of
     * the value of it.pagedList.
     */
    val post = Transformations.switchMap(repoResult, { it.pagedList })
    val networkState = Transformations.switchMap(repoResult, { it.networkState })
    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })

    /**
     * On Pull to Refresh from Ui
     */
    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }

    fun getCurrentSubDelivery(): String? = subDeliveryName.value

    /**
     * Method add favourite item of Delivery List  into db
     *
     * Specific method for DeliveryDetailFragment
     */
    fun setFav(itemData: DeliveryResponseModel) {
        repository.setFavInDb(itemData)
    }

    fun showSubdelivery(subDelivery: String): Boolean {
        if (subDeliveryName.value == subDelivery)
            return false
        else {
            subDeliveryName.value = subDelivery
            return true
        }
    }

}
