package com.example.paging3app.data.room_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paging3app.data.room_db.entities.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remotekeysentity WHERE repoId = :id")
    suspend fun remoteKeysPassenger(id: String): RemoteKeysEntity?

    @Query("DELETE FROM RemoteKeysEntity")
    suspend fun clearRemoteKeys()
}