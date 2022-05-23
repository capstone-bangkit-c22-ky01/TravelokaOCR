package com.example.travelokaocr.ui.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.api.ApiService
import com.example.travelokaocr.data.repository.AuthenticationRepository
import com.example.travelokaocr.databinding.ActivityLoginBinding
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.register.RegisterActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.AuthenticationViewModel
import com.example.travelokaocr.viewmodel.factory.AuthenticationViewModelFactory
import com.example.travelokaocr.viewmodel.preferences.UserPreference

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupAction()
        itemOnClickListener()

        //CREATE API CONNECTION
        val authenticationViewModelFactory = AuthenticationViewModelFactory(AuthenticationRepository(apiService))
        authenticationViewModel = ViewModelProvider(
            this, authenticationViewModelFactory
        )[AuthenticationViewModel::class.java]

        userPreference = UserPreference(this)

        binding.btnLogin.setOnClickListener {
            loginForm()
        }

    }

    private fun loginForm(){
        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        authenticationViewModel.getLoginUsersResponse(email, password)
        observeLogin()
    }

    private fun observeLogin(){
        authenticationViewModel.loginUsers.observe(this){ response ->
            if (response.isSuccessful){
                if(response.body()?.status.equals("success")){
                    val accessToken = response.body()?.data?.accessToken.toString()
                    val refreshToken = response.body()?.data?.refreshToken.toString()

                    saveLoginSession(accessToken, refreshToken)

                    Toast.makeText(
                        this,
                        "${response.body()?.message}",
                        Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this@LoginActivity, FlightActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(
                        this,
                        "${response.body()?.status}, Message : Failed to login, email/password doesn't match",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }else{
                Toast.makeText(
                    this,
                    "Code : ${response.code()}",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun saveLoginSession(accessToken: String, refreshToken: String){
        userPreference.putDataLogin(Constants.ACCESS_TOKEN, accessToken)
        userPreference.putDataLogin(Constants.REFRESH_TOKEN, refreshToken)
        userPreference.putSessionLogin(Constants.IS_LOGIN, true)
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