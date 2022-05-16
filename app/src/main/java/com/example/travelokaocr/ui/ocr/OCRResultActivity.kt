package com.example.travelokaocr.ui.ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelokaocr.databinding.ActivityOcrresultBinding

class OCRResultActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityOcrresultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrresultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}