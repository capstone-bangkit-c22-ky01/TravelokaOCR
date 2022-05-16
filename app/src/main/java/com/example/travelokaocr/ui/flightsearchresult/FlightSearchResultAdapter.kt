package com.example.travelokaocr.ui.flightsearchresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travelokaocr.data.FlightTicket
import com.example.travelokaocr.databinding.ItemRowSearchResultTicketsBinding

class FlightSearchResultAdapter(private val listUser: ArrayList<FlightTicket>, private val onBuyBtnClick: () -> Unit): RecyclerView.Adapter<FlightSearchResultAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowSearchResultTicketsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        holder.binding.timeDepartTv.text =listUser[position].timeDepart
        holder.binding.cityDepartCodeTv.text =listUser[position].cityDepartCode
        holder.binding.flightDurationTv.text =listUser[position].flightDuration
        holder.binding.flightTypeTv.text =listUser[position].flightType
        holder.binding.timeArriveTv.text =listUser[position].timeArrive
        holder.binding.cityArriveCodeTv.text =listUser[position].cityArriveCode
        holder.binding.priceTv.text =listUser[position].price
        holder.binding.airplaneImageIv.setImageResource(listUser[position].airplaneImage)
        holder.binding.airplaneNameTv.text = listUser[position].airplaneName

        holder.binding.buyBtn.setOnClickListener{
            onBuyBtnClick()
        }

    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemRowSearchResultTicketsBinding) : RecyclerView.ViewHolder(binding.root)

}