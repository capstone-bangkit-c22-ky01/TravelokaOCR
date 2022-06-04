package com.example.travelokaocr.ui.profile

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.databinding.ActivityProfileBinding
import com.example.travelokaocr.ui.historyscreen.HistoryActivity
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.auth.LoginActivity
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    //BIDNING
    private lateinit var binding : ActivityProfileBinding

    //API
    private lateinit var viewModel: AccessProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SETUP
        setupView()
        itemOnClickListener()
//
//        showDataUser(dataUser)

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]
    }

    private fun showDataUser(dataUser: String){
        viewModel.profileUser(dataUser).observe(this){ response ->
            if (response is Resources.Loading) {
                progressBar(true)
            } else if (response is Resources.Error) {
                progressBar(false)
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            } else if (response is Resources.Success) {
                progressBar(false)
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {

                        val username = result.data?.name.toString()
                        val email = result.data?.email.toString()
                        val fotoProfil = result.data?.foto_profil.toString()

                        //SHOW DATA
                        binding.tvUsername.text = username
                        binding.tvEmail.text = email
                        Glide.with(this)
                                .load(fotoProfil)
                                .centerCrop()
                                .into(binding.ivProfilePicture)
                    } else {
                        Log.d("PROFILE", result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                startActivity(Intent(this, FlightActivity::class.java))
            }
            R.id.historyMenu -> {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }
    }

    private fun progressBar(isLoading: Boolean) = with(binding){
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}