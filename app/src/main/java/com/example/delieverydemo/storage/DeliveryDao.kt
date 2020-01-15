package com.example.delieverydemo.storage

import androidx.room.*
import com.example.delieverydemo.delievery.model.DeliveryResponseModel

/**
 *
​
 * Purpose – Data Access Object for the delivery table.
​
 * @author ​Rishabh Gupta
​
 * Created on January 15, 2020
​
 * Modified on January 15, 2020
 *
 * */

@Dao
interface DeliveryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDelivery(delivery: DeliveryResponseModel)

    @Update
    fun updateNote(delivery: DeliveryResponseModel)

    @Delete
    fun deleteNote(delivery: DeliveryResponseModel)

    @Query("DELETE FROM delivery")
    fun deleteAllMovies()


    @Query("SELECT * FROM delivery")
    fun getAllDelivery(): List<DeliveryResponseModel>
}