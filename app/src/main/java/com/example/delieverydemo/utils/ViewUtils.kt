package com.example.delieverydemo.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.delieverydemo.data.network.NetworkState
import com.example.delieverydemo.data.network.StatusCode

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

/**
 * paging extension function
 *
 * Method returns error msg found during webservices
 */
private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

/**
 * paging extension function
 *
 * Method to add Network Change Listener(Change in webservice responses)
 */
fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkState.START)
            report.hasError() -> liveData.postValue(
                NetworkState(StatusCode.ERROR, getErrorMessage(report))
            )
            else -> liveData.postValue(NetworkState.SUCCESS)
        }
    }
    return liveData
}