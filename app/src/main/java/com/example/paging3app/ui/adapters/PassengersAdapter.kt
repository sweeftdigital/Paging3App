package com.example.paging3app.ui.adapters

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R
import com.example.paging3app.data.room_db.entities.PassengerEntity

class PassengersAdapter :
    PagingDataAdapter<PassengerEntity, PassengersAdapter.PassengersViewHolder>(object :
        DiffUtil.ItemCallback<PassengerEntity>() {
        override fun areItemsTheSame(oldItem: PassengerEntity, newItem: PassengerEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PassengerEntity, newItem: PassengerEntity): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PassengersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.passenger_recycler_item, parent, false))

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
        holder.onBindPassenger(getItem(position)!!)
    }

    class PassengersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBindPassenger(passenger: PassengerEntity) {
            with(itemView) {
                val nameView = findViewById<TextView>(R.id.nameTV)
                nameView.text = passenger.name
            }
        }
    }
}