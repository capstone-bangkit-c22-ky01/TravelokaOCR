package com.example.travelokaocr.ui.flightsearchresult

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelokaocr.R
import com.example.travelokaocr.data.FlightTicket
import com.example.travelokaocr.databinding.ActivityFlightSearchResultBinding
import com.example.travelokaocr.ui.homescreen.HomeActivity
import com.example.travelokaocr.ui.ocr.OCRScreenActivity

class FlightSearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlightSearchResultBinding

    private val list = ArrayList<FlightTicket>()

    private val listUsers: ArrayList<FlightTicket>
        get(){
            val dataTimeDepart = resources.getStringArray(R.array.data_dummy_timeDepart)
            val dataCityDepartCode = resources.getStringArray(R.array.data_dummy_cityDepartCode)
            val dataFlightDuration = resources.getStringArray(R.array.data_dummy_flightDuration)
            val dataFlightType = resources.getStringArray(R.array.data_dummy_flightType)
            val dataTimeArrive = resources.getStringArray(R.array.data_dummy_timeArrive)
            val dataCityArriveCode = resources.getStringArray(R.array.data_dummy_cityArriveCode)
            val dataPrice = resources.getStringArray(R.array.data_dummy_price)
            val dataAirplaneImage = resources.obtainTypedArray(R.array.data_dummy_airplaneImage)
            val dataAirplaneName = resources.getStringArray(R.array.data_dummy_airplaneName)

            val listUser = ArrayList<FlightTicket>()

            for (i in dataTimeDepart.indices){
                val flightTicket = FlightTicket(
                    dataTimeDepart[i],
                    dataCityDepartCode[i],
                    dataFlightDuration[i],
                    dataFlightType[i],
                    dataTimeArrive[i],
                    dataCityArriveCode[i],
                    dataPrice[i],
                    dataAirplaneImage.getResourceId(i, -1),
                    dataAirplaneName[i]
                )
                listUser.add(flightTicket)
            }
            return listUser
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlightSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecycleView()

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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

        binding.rvSearchResultTickets.setHasFixedSize(true)

        list.addAll(listUsers)

        val layoutManager = LinearLayoutManager(this)
        binding.rvSearchResultTickets.layoutManager = layoutManager

        val adapter = FlightSearchResultAdapter(list){
            val intent = Intent(this, OCRScreenActivity::class.java)
            startActivity(intent)
        }
        binding.rvSearchResultTickets.adapter = adapter

    }



}