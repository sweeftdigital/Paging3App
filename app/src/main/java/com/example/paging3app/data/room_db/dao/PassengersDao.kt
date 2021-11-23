package com.example.paging3app.data.room_db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3app.data.models.Passenger
import com.example.paging3app.data.room_db.entities.PassengerEntity

@Dao
interface PassengersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPassengers(passengers : List<PassengerEntity>)

    @Query("DELETE FROM passengerentity")
    suspend fun deleteAllPassengers()

    @Query("SELECT * FROM PassengerEntity")
    fun getAllPassengers() : PagingSource<Int, PassengerEntity>
}
