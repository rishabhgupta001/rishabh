package com.example.delieverydemo.data.network

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(val statusCode: StatusCode, val msg: String? = null) {

    companion object {
        val SUCCESS = NetworkState(StatusCode.SUCCESS)
        val START = NetworkState(StatusCode.START)
        fun error(msg: String) = NetworkState(StatusCode.ERROR, msg)
    }

}