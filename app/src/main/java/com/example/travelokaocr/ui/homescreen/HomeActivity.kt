package com.example.travelokaocr.ui.homescreen

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityHomeBinding
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupDateEditText()
    }

    override fun onResume() {
        super.onResume()
        setupAutoTextView()
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

    private fun setupAutoTextView(){

        val dataCity = resources.getStringArray(R.array.data_dummy_city)

        val adapterCity: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, dataCity)

        binding.fromEditText.threshold = 1 //will start working from first character
        binding.fromEditText.setAdapter(adapterCity) //setting the adapter data into the AutoCompleteTextView

        binding.toEditText.threshold = 1 //will start working from first character
        binding.toEditText.setAdapter(adapterCity) //setting the adapter data into the AutoCompleteTextView

        val dataPassengers = resources.getStringArray(R.array.data_dummy_passengers)

        val adapterPassengers: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.dropdown_item_home_activity, dataPassengers)

        binding.passengersEditText.threshold = 1 //will start working from first character
        binding.passengersEditText.setAdapter(adapterPassengers) //setting the adapter data into the AutoCompleteTextView

        val dataSeatClass = resources.getStringArray(R.array.data_dummy_seatClass)

        val adapterSeatClass: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.dropdown_item_home_activity, dataSeatClass)

        binding.seatClassEditText.threshold = 1 //will start working from first character
        binding.seatClassEditText.setAdapter(adapterSeatClass) //setting the adapter data into the AutoCompleteTextView

    }

    private fun setupDateEditText(){

        binding.dateEditText.transformIntoDatePicker(this, "EEEE, dd MMM yyyy", Date())

    }

}

private fun TextInputEditText.transformIntoDatePicker(context: Context, format: String, minDate: Date? = null) {
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
