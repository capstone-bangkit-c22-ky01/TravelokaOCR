package com.example.travelokaocr.ui.main.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.FragmentFlightBinding
import com.example.travelokaocr.ui.flightsearchresult.FlightSearchResultActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class FlightFragment : Fragment(), View.OnClickListener {
    //BINDING
    private var _binding: FragmentFlightBinding? = null
    private val binding get() = _binding!!

    //SESSION
    private lateinit var savedPref: SavedPreference

    private var checkTo = false
    private var checkFrom = false
    private var checkDate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFlightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SETUP
        savedPref = SavedPreference(requireContext())
        setupDateEditText()
        itemOnClickListener()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.searchBtn -> {
                //logout, go to flight search result
                if(checkFrom && checkTo && checkDate){
                    startActivity(Intent(requireActivity(), FlightSearchResultActivity::class.java))
                } else{
                    Toast.makeText(requireContext(), "The form is still empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupAutoTextView()
    }

    private fun setupAutoTextView() {
        val city = resources.getStringArray(R.array.data_dummy_city)
//        val code = resources.getStringArray(R.array.code_city)
//        val cityCode = city.zip(code) { ct, cd -> "$ct-$cd" }

        val adapterCity: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, city)

        //FROM
        binding.fromEditText.threshold = 1 //will start working from first character
        binding.fromEditText.setAdapter(adapterCity) //setting the adapter data into the AutoCompleteTextView
        //when clicked
        binding.fromEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val getFromMix = parent.getItemAtPosition(position).toString()
                val getCity: List<String> = getFromMix.split("-")

                println("getFromMix: $getFromMix")

                val onlyFromCity = getCity[0]
                println("onlyFromCity: $onlyFromCity")

                val onlyFromCode = getCity[1]
                println("onlyFromCode: $onlyFromCode")

                saveDataFromCity(onlyFromCity, onlyFromCode)
            }

        //TO
        binding.toEditText.threshold = 1 //will start working from first character
        binding.toEditText.setAdapter(adapterCity) //setting the adapter data into the AutoCompleteTextView
        //when clicked
        binding.toEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val getToMix = parent.getItemAtPosition(position).toString()
                val getCity: List<String> = getToMix.split("-")

                println("getToMix : $getToMix")

                val onlyToCity = getCity[0]
                println("onlyToCity : $onlyToCity")

                val onlyToCode = getCity[1]
                println("onlyToCode : $onlyToCode")

                saveDataToCity(onlyToCity, onlyToCode)
            }

        //PASSENGERS
        val dataPassengers = resources.getStringArray(R.array.data_dummy_passengers)
        val dataNumber = resources.getStringArray(R.array.data_number_passengers)
        val adapterPassengers: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, dataPassengers)
        //when clicked
        binding.passengersEditText.setText(dataPassengers[0])
        binding.passengersEditText.threshold = 1 //will start working from first character
        binding.passengersEditText.setAdapter(adapterPassengers) //setting the adapter data into the AutoCompleteTextView
        binding.passengersEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val passenger = parent.getItemAtPosition(position).toString()
                println("passenger: $passenger")

                val pax = dataNumber[position]
                println("pax : $pax")

                savedPref.putData(Constants.PAX, pax)
            }

        //SEAT CLASS
        val dataSeatClass = resources.getStringArray(R.array.data_dummy_seatClass)
        val adapterSeatClass: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, dataSeatClass)
        //when clicked
        binding.seatClassEditText.setText(dataSeatClass[0])
        binding.seatClassEditText.threshold = 1 //will start working from first character
        binding.seatClassEditText.setAdapter(adapterSeatClass) //setting the adapter data into the AutoCompleteTextView
        binding.seatClassEditText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val seatClass = parent.getItemAtPosition(position).toString()
                println("searClass : $seatClass")

                savedPref.putData(Constants.SEAT, seatClass)
            }
    }

    private fun saveDataToCity(onlyToCity: String?, codeTo: String?) {
        savedPref.putData(Constants.TO_ONLY_CITY, onlyToCity!!)
        savedPref.putData(Constants.TO_CODE, codeTo!!)

        if (
            savedPref.getData(Constants.TO_ONLY_CITY) != null &&
            savedPref.getData(Constants.TO_CODE) != null
        ){
            checkTo = true
        }
    }

    private fun saveDataFromCity(fromOnlyCity: String?, codeFrom: String?) {
        savedPref.putData(Constants.FROM_ONLY_CITY, fromOnlyCity!!)
        savedPref.putData(Constants.FROM_CODE, codeFrom!!)

        if (
            savedPref.getData(Constants.FROM_ONLY_CITY) != null &&
            savedPref.getData(Constants.FROM_CODE) != null
        ){
            checkFrom = true
        }
    }

    private fun setupDateEditText() {
        binding.dateEditText.transformIntoDatePicker(requireContext(), "EEEE, dd MMM yyyy", Date())
    }

    private fun TextInputEditText.transformIntoDatePicker(
        context: Context,
        format: String,
        minDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.US)
                setText(sdf.format(myCalendar.time))

                val date = binding.dateEditText.text.toString()
                savedPref.putData(Constants.DATE, date)

                if(savedPref.getData(Constants.DATE) != null){
                    checkDate = true
                }
                println("date : $date")
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                minDate?.time?.also { datePicker.minDate = it }
                show()
            }
        }
    }

    private fun itemOnClickListener() {
        binding.searchBtn.setOnClickListener(this)
    }
}