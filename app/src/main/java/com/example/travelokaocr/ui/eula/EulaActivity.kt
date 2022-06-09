package com.example.travelokaocr.ui.eula

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.R

class EulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}