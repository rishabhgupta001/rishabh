package com.example.delieverydemo.data.network

data class NetworkState(val statusCode: StatusCode, val msg: String) {

    companion object {
        val SUCCESS = NetworkState(StatusCode.SUCCESS, "Success")
        val START = NetworkState(StatusCode.START, "Start")
        fun error(msg: String) = NetworkState(StatusCode.ERROR, msg)
    }

}