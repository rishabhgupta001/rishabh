package com.example.delieverydemo.api

interface StatusListener {
    fun onStarted()
    fun onSuccess(T: Any)
    fun onFailure(message: String)
}