package com.example.delieverydemo.data.db

import androidx.paging.DataSource
import androidx.room.*
import com.example.delieverydemo.delivery.model.DeliveryResponseModel

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
    fun insertAll(delivery: ArrayList<DeliveryResponseModel>)

    @Update
    fun updateItem(delivery: DeliveryResponseModel)

    @Query("update delivery set isFavourite = :isFav where id =:id")
    fun upadteItme(id: String, isFav: Boolean)

    @Delete
    fun deleteNote(delivery: DeliveryResponseModel)

    @Query("DELETE FROM delivery")
    fun deleteAllMovies()


    /**
     * Room will generate one that uses an Int key to pull Delivery objects
     * from the database as you scroll. (Here it act as a DataSource)
     */
     @Query("SELECT * FROM delivery")
     fun getAllDelivery(): DataSource.Factory<Int, DeliveryResponseModel>

  /*  @Query("SELECT * FROM delivery")
    fun getAllDelivery(): List<DeliveryResponseModel>*/



}