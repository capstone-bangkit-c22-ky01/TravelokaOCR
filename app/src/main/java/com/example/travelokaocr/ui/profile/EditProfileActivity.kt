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
import com.example.travelokaocr.databinding.ActivityEditProfileBinding
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    //BINDING
    private lateinit var binding: ActivityEditProfileBinding

    //API
    private lateinit var viewModel: AccessProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        itemOnClickListener()

        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val fotoProfil = binding.ivProfilePicture.toString()

        val dataUser = hashMapOf(
                "name" to username,
                "email" to email,
                "foto_profil" to fotoProfil
        )
        showDataUser(dataUser)

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]
    }

    //SHOW DATA
    private fun showDataUser(dataUser: HashMap<String, String>){
        viewModel.updateUser(dataUser).observe(this){ response ->
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

                        binding.edtUsername.hint = username
                        binding.edtEmail.hint = email
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

    //USERNAME CHANGED
    private fun isUsernameChange(dataUser: HashMap<String, String>) {
        viewModel.updateUser(dataUser).observe(this){ response ->
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

                        if (!binding.edtUsername.hint.equals(username)) {
                            binding.edtUsername.hint = username
                            return@observe
                        }
                    } else {
                        Log.d("PROFILE", result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //EMAIL CHANGED
    private fun isEmailChange(dataUser: HashMap<String, String>) {
        viewModel.updateUser(dataUser).observe(this){ response ->
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

                        val email = result.data?.email.toString()

                        if (!binding.edtEmail.hint.equals(email)) {
                            binding.edtEmail.hint = email
                            return@observe
                        }
                    } else {
                        Log.d("PROFILE", result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //PHOTO PROFILE CHANGED
    private fun isPhotoChange(dataUser: HashMap<String, String>) {
        viewModel.updateUser(dataUser).observe(this){ response ->
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

                        val email = result.data?.email.toString()

                        if (!binding.edtEmail.hint.equals(email)) {
                            binding.edtEmail.hint = email
                            return@observe
                        }
                    } else {
                        Log.d("PROFILE", result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun update() {
        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()
        val fotoProfil = binding.ivProfilePicture.toString()

        val dataUser = hashMapOf(
                "name" to username,
                "email" to email,
                "foto_profil" to fotoProfil
        )

        isUsernameChange(dataUser)
        isEmailChange(dataUser)
        isPhotoChange(dataUser)

        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
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
                update()
                startActivity(Intent(this, ProfileActivity::class.java))
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