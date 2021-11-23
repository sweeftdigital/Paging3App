package com.example.paging3app.data.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.paging3app.data.room_db.dao.PassengersDao
import com.example.paging3app.data.room_db.dao.RemoteKeysDao
import com.example.paging3app.data.room_db.entities.PassengerEntity
import com.example.paging3app.data.room_db.entities.RemoteKeysEntity

@Database(version = 1, entities = [PassengerEntity::class, RemoteKeysEntity::class], exportSchema = false)
abstract class PassengersDatabase : RoomDatabase() {
    abstract fun getPassengersDao() : PassengersDao
    abstract fun getRemoteKeysDao() : RemoteKeysDao

    companion object {
        private const val DB_NAME = "passengers.db"

        fun getInstance(context: Context): PassengersDatabase = buildDatabase(context)

        private fun buildDatabase(context: Context) : PassengersDatabase =
            Room.databaseBuilder(context.applicationContext, PassengersDatabase::class.java, DB_NAME)
                .build()
    }
}