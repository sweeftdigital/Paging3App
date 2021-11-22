package com.example.paging3app.data.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3app.data.ApiService
import com.example.paging3app.data.RetrofitClient
import com.example.paging3app.data.models.Passenger
import retrofit2.HttpException
import java.io.IOException

class PassengersPagingSource : PagingSource<Int, Passenger>() {
    private val apiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
    override fun getRefreshKey(state: PagingState<Int, Passenger>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Passenger> {
        return try {
            val page = params.key ?: STARTING_PAGE
            val response = apiService.getPassengers(page = page, size = params.loadSize).body()?.passengers
            LoadResult.Page(
                data = response!!,
                prevKey = if (page == STARTING_PAGE) null else page,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception : IOException) {
            return LoadResult.Error(exception)
        } catch (exception : HttpException) {
            return LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_PAGE = 0
    }
}