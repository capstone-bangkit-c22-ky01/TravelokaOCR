package com.example.travelokaocr.ui.adapter

import android.R.attr.path
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelokaocr.data.model.flight.Flights
import com.example.travelokaocr.databinding.ItemRowFlightBinding
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import java.text.NumberFormat
import java.util.*


class SearchListAdapter(
    val context: Context
) : RecyclerView.Adapter<SearchListAdapter.ListUsersViewHolder>() {

    private lateinit var savedPref: SavedPreference

    private val differCallback = object: DiffUtil.ItemCallback<Flights>(){
        override fun areItemsTheSame(
            oldItem: Flights,
            newItem: Flights
        ): Boolean {
            //COMPARE ID BECAUSE IT'S UNIQUE
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Flights,
            newItem: Flights
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differAsync = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((Flights) -> Unit)? = null

    inner class ListUsersViewHolder(var binding: ItemRowFlightBinding): RecyclerView
    .ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersViewHolder {
        val binding = ItemRowFlightBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        return ListUsersViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListUsersViewHolder, position: Int) {
        val data = differAsync.currentList[position]
        savedPref = SavedPreference(context)

        holder.itemView.apply {
            Glide
                .with(context)
                .load(data.icon)
                .apply(RequestOptions().override(32, 32))
                .into(holder.binding.airplaneImageIv)
            holder.binding.timeDepartTv.text = data.depart_time
            holder.binding.timeArriveTv.text = data.arrival_time
            holder.binding.airplaneNameTv.text = data.airline

            val myIndonesianLocale = Locale("in", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale)
            numberFormat.maximumFractionDigits = 0;
            val convert = numberFormat.format(data.price)

            holder.binding.priceTv.text = convert
            holder.binding.cityDepartCodeTv.text = savedPref.getData(Constants.FROM_CODE)
            holder.binding.cityArriveCodeTv.text = savedPref.getData(Constants.TO_CODE)

            val timeDepart = data.depart_time
            val getTimeDepart: List<String> = timeDepart!!.split(":")

            val timeArrive = data.arrival_time
            val getTimeArrive: List<String> = timeArrive!!.split(":")

            val timeDepartToInt = (getTimeDepart[0] + getTimeDepart[1]).toInt()
            println("time depart [0] : $timeDepartToInt")
            println("time depart [1]: ${getTimeDepart[1]}")

            val timeArriveToInt = (getTimeArrive[0] + getTimeArrive[1]).toInt()

            val durationTotal = (timeArriveToInt - timeDepartToInt).toString()
            println("$timeArriveToInt - $timeDepartToInt = $durationTotal")
            println(durationTotal.length)

            var hour = ""
            var minute = ""

            if(durationTotal.length < 3){
                hour = (durationTotal[0]).toString() + (durationTotal[1]).toString()
                minute = (durationTotal[2]).toString() + (durationTotal[3]).toString()
            }else{
                hour = (durationTotal[0]).toString()
                minute = (durationTotal[1]).toString() + (durationTotal[2]).toString()
            }

            holder.binding.flightDurationTv.text = "${hour}h ${minute}m"
            holder.binding.flightTypeTv.text = "Direct"

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