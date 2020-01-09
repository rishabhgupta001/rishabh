package com.example.delieverydemo.api

class NetworkState(val statusCode: StatusCode, val msg: String) {

    companion object {

        val SUCCESS: NetworkState
        val START: NetworkState

        init {
            SUCCESS = NetworkState(StatusCode.SUCCESS, "Success")
            START = NetworkState(StatusCode.START, "Start")
        }
    }

}