package com.example.delieverydemo.storage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delieverydemo.delievery.model.DeliveryResponseModel
import com.example.delieverydemo.storage.paging.DbDeliveryDataSourceFactory
import com.example.delieverydemo.utils.Constants
import com.example.delieverydemo.utils.Constants.DATA_BASE_NAME
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
​
 * Purpose – The Room database that contains the Users table
​
 * @author ​Rishabh Gupta
​
 * Created on January 15, 2020
​
 * Modified on January 15, 2020
 *
 * */

@Database(entities = [DeliveryResponseModel::class], version = 1)
abstract class DeliveryDatabase : RoomDatabase() {
    private lateinit var moviesPaged: LiveData<PagedList<DeliveryResponseModel>>

    //it is a function with NoteDao(Interface) return type
    abstract fun getDeliveryDao(): DeliveryDao

    companion object {

        @Volatile
        private var instance: DeliveryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DeliveryDatabase::class.java,
            DATA_BASE_NAME
        ).build()
    }

    open fun init() {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        val dataSourceFactory = DbDeliveryDataSourceFactory(getDeliveryDao())

        moviesPaged = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .build()
    }

    open fun getMovies(): LiveData<PagedList<DeliveryResponseModel>> {
        return moviesPaged
    }


}