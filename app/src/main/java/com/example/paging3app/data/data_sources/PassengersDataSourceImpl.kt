package com.example.paging3app.data.data_sources

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.paging3app.data.models.Passenger
import com.example.paging3app.data.paging_sources.PassengersPagingSource
import kotlinx.coroutines.flow.Flow

class PassengersDataSourceImpl : PassengersDataSource {
    override suspend fun getPassengers(): Flow<PagingData<Passenger>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            PassengersPagingSource()
        }
    ).flow
}