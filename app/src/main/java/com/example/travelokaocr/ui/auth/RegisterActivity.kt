package com.example.travelokaocr.ui.auth

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.databinding.ActivityRegisterBinding
import com.example.travelokaocr.ui.eula.EulaActivity
import com.example.travelokaocr.ui.main.HomeActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    //BINDING
    private lateinit var binding: ActivityRegisterBinding

    //API
    private lateinit var viewModel: AuthViewModel

    //SESSION
    private lateinit var savedPref: SavedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SETUP
        supportActionBar?.hide()
        nameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        setUpButton()
        itemOnClickListener()

        //CREATE API CONNECTION
        val factory = AuthViewModelFactory(AuthRepository())
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        savedPref = SavedPreference(this)
    }

    private fun nameFocusListener() {
        binding.etvName.setOnFocusChangeListener { _, focused ->
            //WHEN THE EMAIL EDIT TEXT ISN'T FOCUS
            if(!focused){
                binding.tilName.helperText = validateName()
            }
        }
    }

    private fun emailFocusListener() {
        binding.etvEmail.setOnFocusChangeListener { _, focused ->
            //WHEN THE EMAIL EDIT TEXT ISN'T FOCUS
            if(!focused){
                binding.tilEmail.helperText = validateEmail()
            }
        }
    }

    private fun passwordFocusListener() {
        binding.etvPassword.setOnFocusChangeListener { _, focused ->
            //WHEN THE EMAIL EDIT TEXT ISN'T FOCUS
            if(!focused){
                binding.tilPassword.helperText = validatePassword()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_signup -> {
                //go to flight activity
                enableProgressBar()
                signUpForm()
            }
            R.id.login -> {
                //go to login activity
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            R.id.eula_hyperlink -> {
                //go to eula page
                startActivity(Intent(this@RegisterActivity, EulaActivity::class.java))
            }
        }
    }

    @Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //the focus on edit text will be cleared when user touch anything outside the edittext
        if (ev?.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun signUpForm() {
        binding.tilName.helperText = validateName()
        binding.tilEmail.helperText = validateEmail()
        binding.tilPassword.helperText = validatePassword()

        val validName = binding.tilName.helperText == null
        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val name = binding.etvName.text.toString()
        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if(validName && validEmail && validPassword){
            //api process
            val dataRegis = hashMapOf(
                "name" to name,
                "email" to email,
                "password" to password
            )

            val dataLogin = hashMapOf(
                "email" to email,
                "password" to password
            )

            observerRegis(dataRegis, dataLogin)
        } else {
            Toast.makeText(this, "Login gagal, password/email salah", Toast.LENGTH_LONG).show()
            disableProgressBar()
            binding.btnSignup.isEnabled = false
        }
    }

    private fun observerRegis(dataRegis: HashMap<String, String>, dataLogin: HashMap<String, String>) {
        viewModel.regisUser(dataRegis).observe(this) { response ->
            if (response is Resources.Loading) {
                enableProgressBar()
            }
            else if (response is Resources.Error) {
                disableProgressBar()
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        //saved userid
                        val userId = result.data?.user_id.toString()
                        savedPref.putData(Constants.USER_ID, userId)

                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        Log.d("REGIS", result.message.toString())

                        observerLogin(dataLogin)

                    } else {
                        alertUserError(result.message.toString())
                        Log.d("REGIS", result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observerLogin(dataLogin: HashMap<String, String>) {
        viewModel.loginUser(dataLogin).observe(this) { response ->
            if (response is Resources.Loading) {
                enableProgressBar()
            }
            else if (response is Resources.Error) {
                disableProgressBar()
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        //saved userid
                        val accessToken = result.data?.accessToken.toString()
                        val refreshToken = result.data?.refreshToken.toString()
                        saveSessionLogin(accessToken, refreshToken)

                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()

                        //intent to home directly
                        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                        startActivity(intent)
                        killActivity()

                    } else {
                        alertUserError(result.message.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveSessionLogin(accessToken: String, refreshToken: String) {
        savedPref.putData(Constants.ACCESS_TOKEN, accessToken)
        savedPref.putData(Constants.REFRESH_TOKEN, refreshToken)
        savedPref.putSession(Constants.IS_LOGIN, true)
    }

    private fun setUpButton(){
        binding.etvName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                //DO NOTHING
            }
        })

        binding.etvEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                //DO NOTHING
            }
        })

        binding.etvPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                //DO NOTHING
            }

        })
    }

    private fun validateButton() {
        val validName = binding.tilName.helperText == null
        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val name = binding.etvName.text.toString()
        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if (validName && validEmail && validPassword) {
            binding.btnSignup.isEnabled =
                (name.isNotEmpty()) && (email.isNotEmpty()) &&
                        (password.isNotEmpty())
        }
    }

    private fun validateName(): String? {
        val name = binding.etvName.text.toString()

        if (name.isEmpty()) {
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

    private fun itemOnClickListener() {
        binding.btnSignup.setOnClickListener(this)
        binding.login.setOnClickListener(this)
        binding.eulaHyperlink.setOnClickListener(this)
    }

    private fun alertUserError(message: String) {
        val view = View.inflate(this, R.layout.error_400_dialog, null)

        AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
            .setTitle(message)
            .setView(view)
            .setPositiveButton("Back to Register") { p0, _ ->
                p0.dismiss()
            }.show()
    }

    private fun enableProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun disableProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun killActivity() {
        finish()
    }
}