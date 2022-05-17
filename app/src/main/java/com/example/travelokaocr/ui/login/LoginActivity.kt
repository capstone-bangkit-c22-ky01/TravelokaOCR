package com.example.travelokaocr.ui.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.travelokaocr.R
import com.example.travelokaocr.databinding.ActivityLoginBinding
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
        focusEmail()
        focusPassword()
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

    private fun itemOnClickListener(){
        binding.btnLogin.setOnClickListener(this)
        binding.btnLoginWithGoogle.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                startActivity(Intent(this, FlightActivity::class.java))
            }
            R.id.btn_sign_up_with_google -> {

            }
            R.id.sign_up -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }
}