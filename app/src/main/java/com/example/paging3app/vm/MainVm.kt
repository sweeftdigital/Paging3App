package com.example.paging3app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.paging3app.data.data_sources.PassengersDataSource
import com.example.paging3app.data.data_sources.PassengersDataSourceImpl

class MainVm : ViewModel() {
    private val passengersDataSource : PassengersDataSource by lazy {
        PassengersDataSourceImpl()
    }
    suspend fun getPassengers() = passengersDataSource.getPassengers().cachedIn(viewModelScope)
}