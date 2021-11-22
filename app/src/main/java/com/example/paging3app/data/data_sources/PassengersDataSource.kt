package com.example.paging3app.data.data_sources

import androidx.paging.PagingData
import com.example.paging3app.data.models.Passenger
import kotlinx.coroutines.flow.Flow

interface PassengersDataSource {
    suspend fun getPassengers() : Flow<PagingData<Passenger>>
}