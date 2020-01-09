package com.example.delieverydemo.application

import android.app.Application
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
​
 * Purpose – This class is root class for the application
​
 * @author ​Rishabh Gupta
​
 * Created on January 9, 2020
​
 * Modified on January 9, 2020
 *
 * */

class MyApplication : Application() {
    companion object {
        lateinit var mInstance: MyApplication
        lateinit var loggingInterceptor: HttpLoggingInterceptor
    }

    override fun onCreate() {
        super.onCreate()
        //creating instance of MyApplication class
        mInstance = this
        //setUpSingletonObj()
    }

    /**
     * Method to make singleton objects without using dependency injection
     */
    private fun setUpSingletonObj() {
        //for seeing logs of webservices
        loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}