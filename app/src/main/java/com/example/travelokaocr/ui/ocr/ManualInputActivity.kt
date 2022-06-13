package com.example.travelokaocr.ui.ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelokaocr.databinding.ActivityManualInputBinding

class ManualInputActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityManualInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}