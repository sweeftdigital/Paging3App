package com.example.paging3app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R
import com.example.paging3app.data.models.Passenger
import com.example.paging3app.ui.adapters.PassengersAdapter
import com.example.paging3app.vm.MainVm
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val mainVm: MainVm by lazy {
        ViewModelProvider(this).get(MainVm::class.java)
    }
    private var page = 0
    private var isScrolling = false
    private lateinit var passengerRecyclerView : RecyclerView
    private lateinit var passengersAdapter : PassengersAdapter
    private lateinit var linearLayoutManager : LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initPassengersApiCall()
        handleScrolling()
    }

    private fun init() {
        passengerRecyclerView = findViewById(R.id.passengersRecyclerView)
        linearLayoutManager = LinearLayoutManager(this)
    }

    private fun initPassengersApiCall() {
        CoroutineScope(Dispatchers.IO).launch {
            val passengers = mainVm.getPassengers(page) ?: mutableListOf()
            withContext(Dispatchers.Main) {
                initPassengersAdapter(passengers)
            }
        }
    }

    private fun updatePassengersApiCall(page : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val passengers = mainVm.getPassengers(page) ?: mutableListOf()
            withContext(Dispatchers.Main) {
                passengersAdapter.updatePassengers(passengers)

            }
        }
    }

    private fun initPassengersAdapter(passengers : List<Passenger>) {
        passengersAdapter = PassengersAdapter()
        passengersAdapter.updatePassengers(passengers)
        passengerRecyclerView.adapter = passengersAdapter
        passengerRecyclerView.layoutManager = linearLayoutManager
    }

    private fun handleScrolling() {
        passengerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isScrolling && (isAllPassengersScrolled())) {
                    updatePassengersApiCall(page + 1)
                    page ++
                }
            }
        })
    }

    private fun isAllPassengersScrolled() : Boolean {
        val currentItems = linearLayoutManager.childCount
        val totalItems = linearLayoutManager.itemCount
        val scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition()

        return currentItems + scrollOutItems == totalItems
    }
}
