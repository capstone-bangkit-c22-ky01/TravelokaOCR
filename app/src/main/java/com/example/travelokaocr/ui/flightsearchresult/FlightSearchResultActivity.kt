package com.example.travelokaocr.ui.flightsearchresult

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.data.repository.FlightRepository
import com.example.travelokaocr.databinding.ActivityFlightSearchResultBinding
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.factory.FlightViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import com.example.travelokaocr.viewmodel.FlightViewModel

class FlightSearchResultActivity : AppCompatActivity() {

    //BINDING
    private lateinit var binding: ActivityFlightSearchResultBinding

    private lateinit var savedPref: SavedPreference
    private lateinit var list: SearchListAdapter

    //API
    private lateinit var viewModel: FlightViewModel
    private lateinit var authViewModel: AuthViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlightSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedPref = SavedPreference(this)
        setUpView()
        val cityTo = (savedPref.getData(Constants.TO_ONLY_CITY))?.lowercase()
        val cityFrom = (savedPref.getData(Constants.FROM_ONLY_CITY))?.lowercase()
        Log.d("CITY RESULT", "onCreate: $cityTo and $cityFrom")
        val tokenFromAPI = (savedPref.getData(Constants.ACCESS_TOKEN))
        val accessToken = "Bearer $tokenFromAPI"

        //CREATE API CONNECTION
        val factory = FlightViewModelFactory(FlightRepository())
        viewModel = ViewModelProvider(this, factory)[FlightViewModel::class.java]
        observerFlightSearch(accessToken, cityFrom, cityTo)

        val authFactory = AuthViewModelFactory(AuthRepository())
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        binding.fromTv.text = savedPref.getData(Constants.FROM_ONLY_CITY)
        binding.toTv.text = savedPref.getData(Constants.TO_ONLY_CITY)
        binding.dateTv.text = savedPref.getData(Constants.DATE)
        binding.paxTv.text = " · ${savedPref.getData(Constants.PAX)} pax · "
        binding.seatClassTv.text = savedPref.getData(Constants.SEAT)
    }

    private fun observerFlightSearch(accessToken: String, cityFrom: String?, cityTo: String?) {
        viewModel.flightSearch(accessToken, cityFrom!!, cityTo!!).observe(this) { response ->
            if (response is Resources.Loading) {
//                enableProgressBar()
            }
            else if (response is Resources.Error) {
//                disableProgressBar()
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
//                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        list.differAsync.submitList(result.data?.flights)
                    }
                    else {
                        Log.d("REGIS", result.status.toString())
                        val dataToken = hashMapOf(
                            "refreshToken" to savedPref.getData(Constants.REFRESH_TOKEN)
                        )
                        observeUpdateToken(dataToken)
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeUpdateToken(dataToken: HashMap<String, String?>) {
        authViewModel.updateToken(dataToken).observe(this) { response ->
            if (response is Resources.Loading) {
//                enableProgressBar()
            }
            else if (response is Resources.Error) {
//                disableProgressBar()
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
//                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        val cityTo = (savedPref.getData(Constants.TO_ONLY_CITY))?.lowercase()
                        val cityFrom = (savedPref.getData(Constants.FROM_ONLY_CITY))?.lowercase()
                        Log.d("CITY RESULT", "onCreate: $cityTo and $cityFrom")

                        val newAccessToken = result.data?.accessToken.toString()
                        //save new token
                        savedPref.putData(Constants.ACCESS_TOKEN, newAccessToken)

                        //get new token
                        val tokenFromAPI = (savedPref.getData(Constants.ACCESS_TOKEN))
                        val accessToken = "Bearer $tokenFromAPI"

                        observerFlightSearch(accessToken, cityFrom, cityTo)
                    }
                    else {
                        Log.d("REGIS", result.status.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setUpView(){
        list = SearchListAdapter(this)

        //hide the action bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}