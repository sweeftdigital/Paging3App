package com.example.paging3app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R
import com.example.paging3app.data.models.Passenger

class PassengersAdapter :
    PagingDataAdapter<Passenger, PassengersAdapter.PassengersViewHolder>(object :
        DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PassengersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.passenger_recycler_item, parent, false))

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        holder.onBindPassenger(getItem(position)!!)
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