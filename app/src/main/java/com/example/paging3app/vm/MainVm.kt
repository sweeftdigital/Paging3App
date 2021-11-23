package com.example.paging3app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3app.App
import com.example.paging3app.data.data_sources.PassengersDataSource
import com.example.paging3app.data.data_sources.PassengersDataSourceImpl
import com.example.paging3app.data.room_db.PassengersDatabase
import com.example.paging3app.data.room_db.entities.PassengerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainVm : ViewModel() {


}