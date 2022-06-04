package com.example.travelokaocr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelokaocr.data.model.Bookings
import com.example.travelokaocr.data.model.flight.Flights
import com.example.travelokaocr.databinding.ItemRowFlightBinding
import com.example.travelokaocr.databinding.ItemRowHistoryTicketsBinding

class HistoryAdapter(
    val context: Context
) : RecyclerView.Adapter<HistoryAdapter.ListUsersViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<Bookings>(){
        override fun areItemsTheSame(
            oldItem: Bookings,
            newItem: Bookings
        ): Boolean {
            //COMPARE ID BECAUSE IT'S UNIQUE
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Bookings,
            newItem: Bookings
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differAsync = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Bookings) -> Unit)? = null

    inner class ListUsersViewHolder(var binding: ItemRowHistoryTicketsBinding): RecyclerView
    .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersViewHolder {
        val binding = ItemRowHistoryTicketsBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return ListUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListUsersViewHolder, position: Int) {
        val data = differAsync.currentList[position]

        holder.itemView.apply {
            holder.binding.
            Glide.with(this)
                .load(data.icon)
                .into(holder.binding.airplaneImageIv)
            holder.binding.timeDepartTv.text = data.depart_time
            holder.binding.timeArriveTv.text = data.arrival_time
            holder.binding.airplaneNameTv.text = data.airline
            holder.binding.priceTv.text = data.price.toString()

            setOnClickListener {
                onItemClickListener?.let {
                    it(data) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }

}