package com.example.travelokaocr.ui.historyscreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.travelokaocr.data.HistoryTicket
import com.example.travelokaocr.databinding.ItemRowHistoryTicketsBinding


class HistoryTicketsAdapter(private val listTickets: ArrayList<HistoryTicket>): RecyclerView.Adapter<HistoryTicketsAdapter.ListViewHolder>(){

    private val checkMonth = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowHistoryTicketsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val month = listTickets[position].purchaseMonth

        if (checkMonth.isNotEmpty() && checkMonth.contains(month)){

            holder.binding.ticketMonth.visibility = View.GONE

        }else{
            checkMonth.add(month)
            holder.binding.ticketMonth.text = month
        }

        holder.binding.ticketBookingIDNumberTv.text = listTickets[position].bookingID
        holder.binding.ticketPriceTv.text = listTickets[position].price
        holder.binding.cityDepartHistoryTicketTv.text = listTickets[position].cityDepart
        holder.binding.cityArriveHistoryTicketTv.text = listTickets[position].cityArrive

        val purchaseStatus = listTickets[position].purchaseStatus

        holder.binding.statusHistoryTicketTv.text = purchaseStatus

        if (purchaseStatus == "Purchase Pending"){
            holder.binding.statusHistoryTicketTv.setTextColor(Color.parseColor("#F1C40F"))
        }else if (purchaseStatus == "Purchase Successful"){
            holder.binding.statusHistoryTicketTv.setTextColor(Color.parseColor("#27AE60"))
        }

    }

    override fun getItemCount(): Int = listTickets.size

    class ListViewHolder(var binding: ItemRowHistoryTicketsBinding) : RecyclerView.ViewHolder(binding.root)

}