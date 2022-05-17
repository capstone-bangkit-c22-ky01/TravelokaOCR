package com.example.travelokaocr.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityRegisterBinding
import com.example.travelokaocr.ui.homescreen.HomeActivity
import com.example.travelokaocr.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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

    private fun setupAction() {
        focusFullName()
        focusEmail()
        focusPassword()
    }

    private fun focusFullName() {
        binding.etvEmail.setOnFocusChangeListener { _, focus ->
            if(!focus){
                binding.tilFullName.helperText = validateFullName()
            }
        }
    }

    private fun focusEmail() {
        binding.etvEmail.setOnFocusChangeListener { _, focus ->
            if(!focus){
                binding.tilEmail.helperText = validateEmail()
            }
        }
    }

    private fun focusPassword() {
        binding.etvPassword.setOnFocusChangeListener { _, focus ->
            if(!focus){
                binding.tilPassword.helperText = validatePassword()
            }
        }
    }

    private fun validateFullName(): String? {
        val username = binding.etvEmail.text.toString()

        if (username.isEmpty()) {
            return getString(R.string.error_empty_message)
        }
        return null
    }

    private fun validateEmail(): String? {
        val email = binding.etvEmail.text.toString()

        if (email.isEmpty()) {
            return getString(R.string.error_empty_message)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.error_invalid_email_message)
        }
        return null
    }

    private fun validatePassword(): String? {
        val password = binding.etvPassword.text.toString()

        if (password.isEmpty()) {
            return getString(R.string.error_empty_message)
        } else if (password.length < 6) {
            return getString(R.string.error_password_message)
        }
        return null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_sign_up -> {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            R.id.btn_login_with_google -> {

            }
            R.id.login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun itemOnClickListener(){
        binding.btnSignUp.setOnClickListener(this)
        binding.btnSignUpWithGoogle.setOnClickListener(this)
        binding.login.setOnClickListener(this)
    }
}
