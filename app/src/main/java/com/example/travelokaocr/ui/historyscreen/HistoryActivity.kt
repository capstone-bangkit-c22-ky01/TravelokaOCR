package com.example.travelokaocr.ui.historyscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelokaocr.R
import com.example.travelokaocr.data.HistoryTicket
import com.example.travelokaocr.data.repository.TravelokaOCRRepository
import com.example.travelokaocr.databinding.ActivityHistoryBinding
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.profile.ProfileActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.TravelokaOCRViewModel
import com.example.travelokaocr.viewmodel.factory.TravelokaOCRViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val list = ArrayList<HistoryTicket>()

    private val listTickets: ArrayList<HistoryTicket>
        get(){
            val dataMonth = resources.getStringArray(R.array.data_dummy_monthHistoryTicket)
            val dataBookingID = resources.getStringArray(R.array.data_dummy_bookingIDHistoryTicket)
            val dataPrice = resources.getStringArray(R.array.data_dummy_priceHistoryTicket)
            val dataDepartCity = resources.getStringArray(R.array.data_dummy_departCityHistoryTicket)
            val dataArriveCity = resources.getStringArray(R.array.data_dummy_arriveCityHistoryTicket)
            val dataPurchaseStatus = resources.getStringArray(R.array.data_dummy_purchaseStatusHistoryTicket)

            val listTicket = ArrayList<HistoryTicket>()

            for (i in dataMonth.indices){
                val flightTicket = HistoryTicket(
                    dataMonth[i],
                    dataBookingID[i],
                    dataPrice[i],
                    dataDepartCity[i],
                    dataArriveCity[i],
                    dataPurchaseStatus[i],
                )
                listTicket.add(flightTicket)
            }
            return listTicket
        }

    //ADAPTER NEW
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var ocrViewModel: TravelokaOCRViewModel
    private lateinit var savedPreference: SavedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecycleView()

        binding.flightMenu.setOnClickListener {
            val intent = Intent(this, FlightActivity::class.java)
            startActivity(intent)
        }

        binding.profileMenu.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        //CREATE CONNECTION ->
        val ocrViewModelFactory = TravelokaOCRViewModelFactory(TravelokaOCRRepository())
        ocrViewModel = ViewModelProvider(
            this, ocrViewModelFactory
        )[TravelokaOCRViewModel::class.java]

        //GET TOKEN
        savedPreference = SavedPreference(this)
        val tokenFromPreference = savedPreference.getData(Constants.ACCESS_TOKEN)
        val accessToken = "Bearer $tokenFromPreference"

        ocrViewModel.getHistory(accessToken)
        observeHistory()

    }

    private fun observeHistory() {
        ocrViewModel.history.observe(this){ response ->
            if(response.isSuccessful){
                historyAdapter.differAsync.submitList(response.body()?.data)
            } else{
                Log.d("HISTORY", "${response.code()}")
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupRecycleView(){

        list.addAll(listTickets)

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistoryTickets.layoutManager = layoutManager

        val adapter = HistoryTicketsAdapter(list)
        binding.rvHistoryTickets.adapter = adapter

    }

}