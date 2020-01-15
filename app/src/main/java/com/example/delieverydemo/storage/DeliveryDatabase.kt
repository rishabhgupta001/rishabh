package com.example.delieverydemo.storage

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.delieverydemo.delievery.model.DeliveryResponseModel

@Database(entities = [DeliveryResponseModel::class], version = 1)
abstract class DeliveryDatabase : RoomDatabase() {

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
            "deliverydatabase"
        ).build()

    }


}