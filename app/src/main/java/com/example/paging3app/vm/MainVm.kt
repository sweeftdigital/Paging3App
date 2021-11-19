package com.example.paging3app.vm

import androidx.lifecycle.ViewModel
import com.example.paging3app.data.ApiService
import com.example.paging3app.data.RetrofitClient

class MainVm : ViewModel() {
    private val apiService : ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }
    suspend fun getPassengers(page : Int) = apiService.getPassengers(page = page).body()?.passengers
}