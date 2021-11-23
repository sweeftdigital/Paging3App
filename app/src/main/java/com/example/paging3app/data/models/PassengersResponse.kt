package com.example.paging3app.data.models

import com.google.gson.annotations.SerializedName


data class PassengersResponse(
    val totalPassengers : Int,
    val totalPages : Int,
    @SerializedName("data")
    val passengers : List<Passenger>
)

data class Passenger(
    @SerializedName("_id")
    val id : String,
    val name : String
)
