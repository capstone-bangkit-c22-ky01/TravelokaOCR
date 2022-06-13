package com.example.travelokaocr.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelokaocr.data.model.flight.Bookings
import com.example.travelokaocr.databinding.ItemRowHistoryTicketsBinding
import java.text.NumberFormat
import java.util.*

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListUsersViewHolder, position: Int) {
        val data = differAsync.currentList[position]

        holder.itemView.apply {
            holder.binding.ticketBookingIDNumberTv.text = data.booking_code.toString()

            val myIndonesianLocale = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale)
            numberFormat.maximumFractionDigits = 0
            val convert = numberFormat.format(data.price)

            holder.binding.ticketPriceTv.text = convert
            holder.binding.cityDepartHistoryTicketTv.text = data.departure
            holder.binding.cityArriveHistoryTicketTv.text = data.destination

            val status = (data.status)?.replaceFirstChar { it.uppercase() }
            holder.binding.statusHistoryTicketTv.text = "Purchase $status"

            setOnClickListener {
                onItemClickListener?.let {
                    it(data)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }

    fun setOnItemClickListener(listener: (Bookings) -> Unit){
        onItemClickListener = listener
    }

}