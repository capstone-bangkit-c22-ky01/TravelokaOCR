package com.example.travelokaocr.ui.ocr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelokaocr.databinding.ActivityOcrscreenBinding

class OCRScreenActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityOcrscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}