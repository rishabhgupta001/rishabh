package com.example.delieverydemo.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delieverydemo.delivery.model.DeliveryResponseModel
import com.example.delieverydemo.utils.Constants.DATA_BASE_NAME

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

@Database(entities = [DeliveryResponseModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    //it is a function with NoteDao(Interface) return type
    abstract fun getDeliveryDao(): DeliveryDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATA_BASE_NAME
        ).build()
    }

}