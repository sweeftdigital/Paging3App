package com.example.paging3app.data.remote_mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.paging3app.App
import com.example.paging3app.data.ApiService
import com.example.paging3app.data.RetrofitClient
import com.example.paging3app.data.room_db.PassengersDatabase
import com.example.paging3app.data.room_db.entities.PassengerEntity
import com.example.paging3app.data.room_db.entities.RemoteKeysEntity
import java.io.InvalidObjectException
import java.lang.Exception

@ExperimentalPagingApi
class PassengersRemoteMediator : RemoteMediator<Int, PassengerEntity>() {

    private val passengersDatabase by lazy {
        PassengersDatabase.getInstance(App.getAppInstance()!!.applicationContext)
    }

    private val apiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PassengerEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.APPEND -> {
                    val remoteKey =
                        getLastKey(state) ?: throw InvalidObjectException("InvalidObjectException")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestKey(state)
                    remoteKey?.nextKey?.minus(1) ?: INIT_PAGE
                }
            }

            val response = apiService.getPassengers(page, state.config.initialLoadSize)
            val endOfPagination = response.body()?.passengers!!.size < state.config.pageSize

            if (response.isSuccessful) {
                response.body()!!.let { it ->
                    if (loadType == LoadType.REFRESH) {
                        passengersDatabase.getRemoteKeysDao().clearRemoteKeys()
                        passengersDatabase.getPassengersDao().deleteAllPassengers()
                    }

                    val prevKey = if (page == INIT_PAGE) null else page - 1
                    val nextKey = if (endOfPagination) null else page + 1

                    val remoteKeys = it.passengers.map {
                        RemoteKeysEntity(it.id, prevKey, nextKey)
                    }

                    passengersDatabase.getRemoteKeysDao().insertAll(remoteKeys)
                    passengersDatabase.getPassengersDao().insertAllPassengers(it.passengers)

                    return MediatorResult.Success(endOfPaginationReached = true)

                }
            } else {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getClosestKey(state: PagingState<Int, PassengerEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it).let {
                passengersDatabase.getRemoteKeysDao().remoteKeysPassenger(it!!.id)
            }
        }
    }

    private suspend fun getLastKey(state: PagingState<Int, PassengerEntity>): RemoteKeysEntity? {
        return state.lastItemOrNull()?.let {
            passengersDatabase.getRemoteKeysDao().remoteKeysPassenger(it.id)
        }
    }

    companion object {
        const val INIT_PAGE = 0
    }

}