package com.example.travelokaocr.ui.homescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelokaocr.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}