package com.example.delieverydemo.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.delieverydemo.data.network.NetworkState

/**
 *
​
 * Purpose – Data class that is necessary for a UI to show a listing and interact with network and user
 *
 * @author ​Rishabh Gupta
​
 * Created on January 28, 2020
​
 * Modified on January 28, 2020
 *
 * */
data class Listing<T>(
    // the LiveData of paged lists for the UI to observe into Recyclerview
    val pagedList: LiveData<PagedList<T>>,
    //represent network request status
    val networkState: LiveData<NetworkState>,
    //for refresh data (different from networkState)
    val refreshState: LiveData<NetworkState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries if any failed requests.
    val retry: () -> Unit
)