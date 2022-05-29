package com.example.travelokaocr.ui.profile

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
        binding.tvEditProfile.setOnClickListener(this)
        binding.tvUploadImage.setOnClickListener(this)
        binding.btnSaveChanges.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_edit_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.tv_upload_image -> {

            }
            R.id.btn_save_changes -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
    }
}