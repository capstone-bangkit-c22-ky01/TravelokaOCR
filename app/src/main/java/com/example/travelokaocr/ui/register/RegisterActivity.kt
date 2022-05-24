package com.example.travelokaocr.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.api.ApiService
import com.example.travelokaocr.data.repository.AuthenticationRepository
import com.example.travelokaocr.databinding.ActivityRegisterBinding
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.login.LoginActivity
import com.example.travelokaocr.viewmodel.AuthenticationViewModel
import com.example.travelokaocr.viewmodel.factory.AuthenticationViewModelFactory

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authenticationViewModelFactory = AuthenticationViewModelFactory(AuthenticationRepository(apiService))
        viewModel = ViewModelProvider(
            this, authenticationViewModelFactory
        )[AuthenticationViewModel::class.java]

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
                startActivity(Intent(this, FlightActivity::class.java))
            }
            R.id.btn_login_with_google -> {
                registerForm()
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

    private fun registerForm() {
        binding.tilFullName.helperText = validateFullName()
        binding.tilEmail.helperText = validateEmail()
        binding.tilPassword.helperText = validatePassword()

        val validName = binding.tilFullName.helperText == null
        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val fullName = binding.etvFullName.text.toString()
        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if(validName && validEmail && validPassword){
            viewModel.getRegisterUsersResponse(fullName, email, password)
            observeRegister()
        }
    }

    private fun observeRegister() {
        viewModel.registerUsers.observe(this){ response ->
            if (response!!.isSuccessful) {
                if(response.body()?.status.equals("success")){
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(
                        this,
                        "${response.body()?.status}, Message : Failed to register",
                        Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Code : ${response.code()}",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
