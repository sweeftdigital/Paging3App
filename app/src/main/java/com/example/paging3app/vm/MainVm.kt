package com.example.paging3app.vm

import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.example.paging3app.App
import com.example.paging3app.data.remote_mediators.PassengersRemoteMediator
import com.example.paging3app.data.room_db.PassengersDatabase
import com.example.paging3app.data.room_db.entities.PassengerEntity
import kotlinx.coroutines.flow.Flow

class MainVm : ViewModel() {

    private val passengerDao by lazy {
        PassengersDatabase.getInstance(App.getAppInstance()!!.applicationContext).getPassengersDao()
    }

    @ExperimentalPagingApi
    fun getPassengersFromDb() : Flow<PagingData<PassengerEntity>> {
        return  Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = 1000
            ),
            remoteMediator = PassengersRemoteMediator()
        ) {
            passengerDao.getAllPassengers()
        }.flow
    }

   companion object {
       private const val PAGE_SIZE = 100
   }

}