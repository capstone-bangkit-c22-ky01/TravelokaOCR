package com.example.travelokaocr.ui.ocr

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivitySuccessPageBinding
import com.example.travelokaocr.ui.main.fragment.HistoryFragment
import com.example.travelokaocr.ui.main.fragment.ProfileFragment

class SuccessPageActivity : AppCompatActivity() {
    //BINDING
    private lateinit var binding: ActivitySuccessPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.cvBtnSuccess.setOnClickListener {
            val fragment = HistoryFragment()
            supportFragmentManager.beginTransaction().replace(R.id.iv_success_page_layout, fragment).commit()
            binding.cvBtnSuccess.visibility = View.GONE
        }

    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}