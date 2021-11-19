package com.example.paging3app.data

import com.example.paging3app.data.models.PassengersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(".")
    suspend fun getPassengers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PassengersResponse>
}