package com.example.paging3app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R
import com.example.paging3app.data.room_db.PassengersDatabase
import com.example.paging3app.ui.adapters.PassengerLoadStateAdapter
import com.example.paging3app.ui.adapters.PassengersAdapter
import com.example.paging3app.vm.MainVm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val mainVm: MainVm by lazy {
        ViewModelProvider(this).get(MainVm::class.java)
    }

    private lateinit var passengerRecyclerView: RecyclerView
    private lateinit var passengersAdapter: PassengersAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initPassengersAdapter()

    }

    private fun init() {
        passengerRecyclerView = findViewById(R.id.passengersRecyclerView)
        linearLayoutManager = LinearLayoutManager(this)
    }

    private fun initPassengersAdapter() {
        passengersAdapter = PassengersAdapter()
        passengerRecyclerView.adapter = passengersAdapter.withLoadStateHeaderAndFooter(
            header = PassengerLoadStateAdapter { passengersAdapter.retry() },
            footer = PassengerLoadStateAdapter { passengersAdapter.retry() }
        )
        passengerRecyclerView.layoutManager = linearLayoutManager
    }

}
