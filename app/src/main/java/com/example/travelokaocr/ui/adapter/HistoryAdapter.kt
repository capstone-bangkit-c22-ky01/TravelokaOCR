package com.example.travelokaocr.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelokaocr.data.model.Bookings
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
            holder.binding.ticketBookingIDNumberTv.text = data.booking_code.toString()
            holder.binding.ticketPriceTv.text = data.price.toString()
            holder.binding.cityDepartHistoryTicketTv.text = data.departure
            holder.binding.cityArriveHistoryTicketTv.text = data.destination
            holder.binding.statusHistoryTicketTv.text = data.status

        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }

}