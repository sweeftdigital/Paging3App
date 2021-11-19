package com.example.paging3app.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R
import com.example.paging3app.data.models.Passenger

class PassengersAdapter :
    RecyclerView.Adapter<PassengersAdapter.PassengersViewHolder>() {

    private val passengers: MutableList<Passenger> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PassengersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.passenger_recycler_item, parent, false)
        )

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        holder.onBindPassenger(passengers[position])
    }

    override fun getItemCount() = passengers.size

    @SuppressLint("NotifyDataSetChanged")
    fun updatePassengers(passengers : List<Passenger>) {
        this.passengers.addAll(passengers)
        this.notifyDataSetChanged()
    }

    class PassengersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBindPassenger(passenger: Passenger) {
            with(itemView) {
                val nameView = findViewById<TextView>(R.id.nameTV)
                val tripsView = findViewById<TextView>(R.id.tripsTV)

                nameView.text = passenger.name
                tripsView.text = passenger.trips.toString()
            }
        }
    }
}