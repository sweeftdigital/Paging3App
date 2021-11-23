package com.example.paging3app.data.room_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PassengerEntity(
    @PrimaryKey
    val id : String,
    val name : String,
)
