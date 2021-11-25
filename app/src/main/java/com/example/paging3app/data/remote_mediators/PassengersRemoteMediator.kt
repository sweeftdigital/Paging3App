package com.example.paging3app.data.remote_mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paging3app.App
import com.example.paging3app.data.ApiService
import com.example.paging3app.data.RetrofitClient
import com.example.paging3app.data.models.Passenger
import com.example.paging3app.data.room_db.PassengersDatabase
import com.example.paging3app.data.room_db.entities.PassengerEntity
import com.example.paging3app.data.room_db.entities.RemoteKeysEntity
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class PassengersRemoteMediator : RemoteMediator<Int, PassengerEntity>() {

    private val passengersDatabase by lazy {
        PassengersDatabase.getInstance(App.getAppInstance()!!.applicationContext)
    }

    private val apiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PassengerEntity>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiService.getPassengers(page, state.config.pageSize)
            val passengers = response.body()?.passengers
            val isEndOfList = passengers?.isEmpty()
            passengersDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    passengersDatabase.getPassengersDao().deleteAllPassengers()
                    passengersDatabase.getRemoteKeysDao().clearRemoteKeys()
                }
                val prevKey = if (page == INIT_PAGE) null else page - 1
                val nextKey = if (isEndOfList!!) null else page + 1
                val keys = passengers.map {
                    RemoteKeysEntity(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                passengersDatabase.getRemoteKeysDao().insertAll(keys)
                passengersDatabase.getPassengersDao().insertAllPassengers(mapToPassengersEntity(passengers))
            }
            return MediatorResult.Success(endOfPaginationReached = false)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, PassengerEntity>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: INIT_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, PassengerEntity>): RemoteKeysEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { passenger -> passengersDatabase.getRemoteKeysDao().remoteKeysPassenger(passenger.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, PassengerEntity>): RemoteKeysEntity? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { passenger -> passengersDatabase.getRemoteKeysDao().remoteKeysPassenger(passenger.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, PassengerEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                passengersDatabase.getRemoteKeysDao().remoteKeysPassenger(repoId)
            }
        }
    }

    private fun mapToPassengersEntity(passengers: List<Passenger>): List<PassengerEntity> =
        passengers.map {
            PassengerEntity(it.id, it.name)
        }

    companion object {
        const val INIT_PAGE = 0
    }

}