package com.example.paging3app.data.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RemoteKeysEntity(
    @PrimaryKey val repoId : String,
    val prevKey : Int?,
    val nextKey : Int?
)