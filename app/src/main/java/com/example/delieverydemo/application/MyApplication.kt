package com.example.delieverydemo.application

import android.app.Application
import com.example.delieverydemo.data.db.AppDatabase
import com.example.delieverydemo.data.network.ApiService
import com.example.delieverydemo.data.network.NetworkConnectionInterceptor
import com.example.delieverydemo.data.preference.Pref
import com.example.delieverydemo.data.repositories.DeliveryRepository
import com.example.delieverydemo.ui.delivery.viewmodelfactory.DeliveryViewModelFactory
import com.example.delieverydemo.utils.AppExecutor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

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

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        //use operator fun in singleton classes mostly act as a constructor
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton {
            DeliveryRepository(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind() from singleton { Pref(instance()) }
        bind() from singleton { AppExecutor() }


        bind() from provider { DeliveryViewModelFactory(instance()) }
        //bind() from provider { DeliveryBoundaryCallback(instance(), instance()) }
        /*  bind() from provider {
              DeliveryBoundaryCallback2(
                  instance(),
                  instance(),
                  instance(),
                  instance(), instance()
              )
          }*/
    }
}