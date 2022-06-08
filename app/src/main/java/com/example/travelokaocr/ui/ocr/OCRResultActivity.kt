package com.example.travelokaocr.ui.ocr

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.travelokaocr.R
import com.example.travelokaocr.data.model.ocr.DataKTPResult
import com.example.travelokaocr.databinding.ActivityOcrresultBinding
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.preference.SavedPreference

class OCRResultActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityOcrresultBinding

    private lateinit var savedPreference: SavedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        savedPreference = SavedPreference(this)
        val tokenFromPreferences = savedPreference.getData(Constants.ACCESS_TOKEN)
        val accessToken = "Bearer $tokenFromPreferences"

        val dataKTPResult = intent.getParcelableExtra<DataKTPResult?>("IDCardResult") as DataKTPResult

        setupResult(
            dataKTPResult.nik,
            dataKTPResult.name,
            dataKTPResult.sex,
            dataKTPResult.married,
            dataKTPResult.nationality,
            dataKTPResult.title
        )


    }

    override fun onResume() {
        super.onResume()
        setupAutoTextView()
    }

    private fun setupResult(
        nikFromAPI: String?,
        nameFromAPI: String?,
        sexFromAPI: String?,
        marriedFromAPI: String?,
        nationalityFromAPI: String?,
        titleFromAPI: String?) {

        //FOR NIK
        binding.edtNik.setText(nikFromAPI.toString())

        //FOR NAME
        binding.edtName.setText(nameFromAPI)

        //FOR TITLE PART
        val title = resources.getStringArray(R.array.data_title)

        when(titleFromAPI){
            "Mr." -> binding.edtTitle.setText(title[0])
            "Ms." -> binding.edtTitle.setText(title[1])
            "Mrs." -> binding.edtTitle.setText(title[2])
        }

        val arrayAdapterTitle = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, title)
        binding.edtTitle.setAdapter(arrayAdapterTitle)

        //FOR NATIONALITY PART
        val nationality = resources.getStringArray(R.array.data_nationality)

        when(nationalityFromAPI){
            "WNI" -> binding.edtNationality.setText(nationality[0])
            "WNA" -> binding.edtNationality.setText(nationality[1])
        }

        val arrayAdapterNationality = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, nationality)
        binding.edtNationality.setAdapter(arrayAdapterNationality)

        //FOR GENDER PART
        val gender = resources.getStringArray(R.array.data_gender)

        when(sexFromAPI){
            "Male" -> binding.edtGender.setText(gender[0])
            "Female" -> binding.edtGender.setText(gender[1])
        }

        val arrayAdapterGender = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, gender)
        binding.edtGender.setAdapter(arrayAdapterGender)

        //FOR MARITAL STATUS PART
        val maritalStatus = resources.getStringArray(R.array.data_marital_status)

        when(marriedFromAPI){
            "Single" -> binding.edtMaritalStatus.setText(maritalStatus[0])
            "Married" -> binding.edtMaritalStatus.setText(maritalStatus[1])
        }

        val arrayAdapterMaritalStatus = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, maritalStatus)
        binding.edtMaritalStatus.setAdapter(arrayAdapterMaritalStatus)
    }


    private fun setupAutoTextView() {
        //FOR TITLE PART
        val title = resources.getStringArray(R.array.data_title)
        binding.edtTitle.setText(title[1])
        val arrayAdapterTitle = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, title)
        binding.edtTitle.setAdapter(arrayAdapterTitle)

        //FOR NATIONALITY PART
        val nationality = resources.getStringArray(R.array.data_nationality)
        val arrayAdapterNationality = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, nationality)
        binding.edtNationality.setAdapter(arrayAdapterNationality)

        //FOR GENDER PART
        val gender = resources.getStringArray(R.array.data_gender)
        val arrayAdapterGender = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, gender)
        binding.edtGender.setAdapter(arrayAdapterGender)

        //FOR MARITAL STATUS PART
        val maritalStatus = resources.getStringArray(R.array.data_marital_status)
        val arrayAdapterMaritalStatus = ArrayAdapter(this, R.layout.dropdown_item_ocr_result, maritalStatus)
        binding.edtMaritalStatus.setAdapter(arrayAdapterMaritalStatus)
    }


}