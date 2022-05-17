package com.example.travelokaocr.ui.profile

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityProfileBinding
import com.example.travelokaocr.ui.historyscreen.HistoryActivity
import com.example.travelokaocr.ui.homescreen.HomeActivity
import com.example.travelokaocr.ui.login.LoginActivity

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        itemOnClickListener()
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

    private fun itemOnClickListener(){
        binding.btnEditProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.tvAboutTraveloka.setOnClickListener(this)

        binding.flightMenu.setOnClickListener(this)
        binding.historyMenu.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_profile -> {
                startActivity(Intent(this, EditProfileActivity::class.java))
            }
            R.id.btn_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.tv_about_traveloka -> {

            }

            R.id.flightMenu -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.historyMenu -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
    }
}