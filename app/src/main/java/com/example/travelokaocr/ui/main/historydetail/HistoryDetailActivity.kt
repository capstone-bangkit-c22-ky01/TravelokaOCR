package com.example.travelokaocr.ui.main.historydetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.data.repository.FlightRepository
import com.example.travelokaocr.databinding.ActivityHistoryDetailBinding
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.FlightViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.factory.FlightViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import java.text.NumberFormat
import java.util.*

class HistoryDetailActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityHistoryDetailBinding

    //SESSION
    private lateinit var savedPref: SavedPreference
    private lateinit var accessToken: String

    //API
    private lateinit var viewModel: FlightViewModel
    private lateinit var authViewModel: AuthViewModel

    //PARSE DATA
    private val args: HistoryDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //CREATE API CONNECTION
        val factory = FlightViewModelFactory(FlightRepository())
        viewModel = ViewModelProvider(this, factory)[FlightViewModel::class.java]
        val authFactory = AuthViewModelFactory(AuthRepository())
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        //SETUP
        savedPref = SavedPreference(this)
        val tokenFromApi = savedPref.getData(Constants.ACCESS_TOKEN)
        accessToken = "Bearer $tokenFromApi"

        val bookingID = args.bookingid

        observeDetailHistory(bookingID, accessToken)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivDelete.setOnClickListener {
            alertDelete(bookingID, accessToken)
        }

    }

    private fun observeDetailHistory(bookingID: String, accessToken: String) {
        viewModel.detailHistory(bookingID, accessToken).observe(this) { response ->
            if (response is Resources.Loading) {
                //DO NOTHING
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
                        binding.fromTv.text = result.data?.departure
                        binding.toTv.text = result.data?.destination
                        binding.tvBookingCode.text = (result.data?.booking_code).toString()

                        Glide
                            .with(this)
                            .load(result.data?.icon)
                            .apply(RequestOptions().override(32, 32))
                            .into(binding.ivDestination)

                        binding.tvAirplaneName.text = result.data?.airline
                        binding.tvDepartTime.text = result.data?.depart_time
                        binding.tvArrivalTime.text = result.data?.arrival_time

                        binding.tvDeparture.text = result.data?.departure
                        binding.tvDestination.text = result.data?.destination

                        if((result.data?.passenger_title != null) && (result.data.passenger_name != null)){
                            binding.tvDefaultStatus.text = result.data.passenger_title
                            binding.tvDefaultUser.text = result.data.passenger_name
                        }else{
                            binding.tvDefaultStatus.text = result.data?.passenger_title
                            binding.tvDefaultUser.text = getString(R.string.detail_user_info)
                            binding.tvDefaultUser.setTextColor(ContextCompat.getColor(this, R.color.customColorFont))
                            binding.purchaseStatus.setBackgroundResource(R.color.failed)
                        }

                        //CONVERT PRICE TO RP
                        val myIndonesianLocale = Locale("in", "ID")
                        val numberFormat = NumberFormat.getCurrencyInstance(myIndonesianLocale)
                        numberFormat.maximumFractionDigits = 0
                        val convert = numberFormat.format(result.data?.price)
                        binding.tvPrice.text = convert

                        val status = result.data?.status

                        if(status == "success"){
                            binding.tvStatus.text = String.format(getString(R.string.HistoryAdapter_PurchaseStatus, status))
                            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.customColorFont))
                            binding.purchaseStatus.setBackgroundResource(R.color.success)
                        } else{
                            binding.tvStatus.text = String.format(getString(R.string.HistoryAdapter_PurchaseStatus, status))
                            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.customColorFont))
                            binding.purchaseStatus.setBackgroundResource(R.color.pending)
                        }

                    }
                    else {
                        println("Result : ${result.status.toString()}")

                        val tokenFromApi = savedPref.getData(Constants.REFRESH_TOKEN)
                        println("refresh token : $tokenFromApi")

                        val dataToken = hashMapOf(
                            "refreshToken" to tokenFromApi
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
                        val newAccessToken = result.data?.accessToken.toString()
                        println("new access token : $newAccessToken")

                        //save new token
                        savedPref.putData(Constants.ACCESS_TOKEN, newAccessToken)

                        //get new token
                        val tokenFromAPI = (savedPref.getData(Constants.ACCESS_TOKEN))
                        println("token from api : $tokenFromAPI")

                        val accessToken = "Bearer $tokenFromAPI"
                        println("access token : $accessToken")

                        val bookingID = args.bookingid

                        observeDetailHistory(bookingID, accessToken)
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

    private fun alertDelete(bookingID: String, accessToken: String) {
        viewModel.deleteBookingById(bookingID, accessToken).observe(this) { response ->
            if (response is Resources.Loading){
                progressBar(true)
            }
            else if (response is Resources.Error){
                progressBar(false)
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                val result = response.data
                if (result != null){
                    if (result.status == "success"){

                        val view = View.inflate(this, R.layout.delete_dialog, null)

                        AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
                            .setView(view)
                            .setNegativeButton("No"){ _, _ ->
                                //DO NOTHING
                            }
                            .setPositiveButton("YES") {_, _ ->
                                //Intent to History Fragment
                                finish()
                            }
                            .show()
                        progressBar(false)
                    }
                    else {
                        val dataToken = hashMapOf(
                            "refreshToken" to savedPref.getData(Constants.REFRESH_TOKEN)
                        )
                        Log.d("REFRESH TOKEN", "observerFlightSearch: $dataToken")
                        Log.d("ACCESS TOKEN", "observerFlightSearch: $accessToken")
                        observeUpdateToken(dataToken)
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun progressBar(isLoading: Boolean) = with(binding){
        if (isLoading) {
            this.progressBar.visibility = View.VISIBLE
        } else {
            this.progressBar.visibility = View.GONE
        }
    }

}