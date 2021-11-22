package com.example.paging3app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3app.R

class PassengerLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PassengerLoadStateAdapter.PassengerLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PassengerLoadStateViewHolder {
        return PassengerLoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.passenger_load_state_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PassengerLoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    inner class PassengerLoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.findViewById<Button>(R.id.retryBtn).setOnClickListener {
                retry.invoke()
            }
        }

        fun onBind(loadState: LoadState) {
            with(itemView) {
                val loadProgressBar = findViewById<ProgressBar>(R.id.loadProgressBar)
                val retryBtn = findViewById<Button>(R.id.retryBtn)
                val loadErrorMessageView = findViewById<TextView>(R.id.loadErrorMessageView)

                loadProgressBar.isVisible = loadState is LoadState.Loading
                retryBtn.isVisible = loadState !is LoadState.Loading
                loadErrorMessageView.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}