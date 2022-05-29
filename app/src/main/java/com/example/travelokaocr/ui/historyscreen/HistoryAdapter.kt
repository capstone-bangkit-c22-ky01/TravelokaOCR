package com.example.travelokaocr.ui.historyscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelokaocr.data.model.UserDataHistory
import com.example.travelokaocr.data.model.UserDataHistoryNew
import com.example.travelokaocr.databinding.ItemRowHistoryTicketsBinding

class HistoryAdapter(
    val context: Context
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    private val differCallback = object: DiffUtil.ItemCallback<UserDataHistoryNew>(){
        override fun areItemsTheSame(
            oldItem: UserDataHistoryNew,
            newItem: UserDataHistoryNew
        ): Boolean {
            return oldItem.bookingID == newItem.bookingID
        }

        override fun areContentsTheSame(
            oldItem: UserDataHistoryNew,
            newItem: UserDataHistoryNew
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differAsync = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((UserDataHistory) -> Unit)? = null

    inner class HistoryViewHolder(var binding: ItemRowHistoryTicketsBinding): RecyclerView
    .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemRowHistoryTicketsBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = differAsync.currentList[position]

        holder.itemView.apply {
            holder.binding.ticketBookingIDNumberTv.text = history.bookingID
            holder.binding.ticketMonth.text = history.purchaseMonth
            holder.binding.ticketPriceTv.text = history.price
            holder.binding.cityDepartHistoryTicketTv.text = history.cityDepart
            holder.binding.cityArriveHistoryTicketTv.text = history.cityArrive
        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }
}