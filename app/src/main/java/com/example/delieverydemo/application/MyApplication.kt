package com.example.delieverydemo.application

import android.app.Application
import com.example.delieverydemo.api.ApiService
import com.example.delieverydemo.api.NetworkConnectionInterceptor
import com.example.delieverydemo.delievery.datasource.DeliveryDataSouce
import com.example.delieverydemo.delievery.datasource.factory.DeliveryDataSourceFactory
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.kodein.di.Kodein

/**
 *
​
 * Purpose – This class is root class for the application
​
 * @author ​Rishabh Gupta
​
 * Created on January 9, 2020
​
 * Modified on January 15, 2020
 *
 * */

class MyApplication : Application(), KodeinAware {
    companion object {
        lateinit var mInstance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        //creating instance of MyApplication class
        mInstance = this
    }



    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiService() }


        //bind() from provider { LoginViewModelFactory(instance()) }
    }
}