package com.example.travelokaocr.ui.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.databinding.ActivityLoginBinding
import com.example.travelokaocr.ui.main.HomeActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

const val RC_SIGN_IN = 200

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //BINDING
    private lateinit var binding: ActivityLoginBinding

    //API
    private lateinit var viewModel: AuthViewModel

    //SESSION
    private lateinit var savedPref: SavedPreference

    private lateinit var gsc: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        if(savedPref.getSession(Constants.IS_LOGIN)){
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            killActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SETUP
        setUpView()
        setUpButton()
        setUpEditText()
        itemOnClickListener()

        //CREATE API CONNECTION
        val factory = AuthViewModelFactory(AuthRepository())
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        savedPref = SavedPreference(this)

        //GOOGLE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        gsc = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            loginWithGoogle()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_login -> {
                //go to flight activity
                enableProgressBar()
                loginForm()
            }
            R.id.btn_login_with_google -> {
                //go to login auth
                enableProgressBar()
                googleLogin()
            }
            R.id.sign_up -> {
                //go to sign up activity
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun loginForm() {
        binding.tilEmail.helperText = validatePassword()
        binding.tilPassword.helperText = validatePassword()

        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if(validEmail && validPassword){
            //api process
            val dataLogin = hashMapOf(
                "email" to email,
                "password" to password
            )

            observerLogin(dataLogin)
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
                        Log.d("REGIS", result.message.toString())

                        //intent to home directly
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        killActivity()

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

    private fun saveSessionLogin(accessToken: String, refreshToken: String) {
        savedPref.putData(Constants.ACCESS_TOKEN, accessToken)
        savedPref.putData(Constants.REFRESH_TOKEN, refreshToken)
        savedPref.putSession(Constants.IS_LOGIN, true)
    }

    private fun setUpButton() {
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

    private fun googleLogin() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginWithGoogle() {

        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        val dataLogin = hashMapOf(
                "email" to email,
                "password" to password
        )

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
                if (result?.status.equals("success")) {
                    //saved userid
                    val accessToken = result?.data?.accessToken.toString()
                    val refreshToken = result?.data?.refreshToken.toString()
                    saveSessionLogin(accessToken, refreshToken)

                    Toast.makeText(this, result?.message, Toast.LENGTH_SHORT).show()
                    Log.d("REGIS", result?.message.toString())

                    //intent to home directly
                    //home activity still under the development
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    killActivity()
                }
            }
        }
    }

    private fun validateButton() {
        binding.tilEmail.helperText = validateEmail()
        binding.tilPassword.helperText = validatePassword()

        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if (validEmail && validPassword) {
            binding.btnLogin.isEnabled =
                (email.isNotEmpty()) &&
                        (password.isNotEmpty())
        }
    }

    private fun setUpEditText() {
        //FOR EMAIL
        binding.etvEmail.setOnFocusChangeListener { _, focus ->
            if(!focus){
                binding.tilEmail.helperText = validateEmail()
            }
        }

        //FOR PASSWORD
        binding.etvPassword.setOnFocusChangeListener { _, focus ->
            if (!focus) {
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

    private fun itemOnClickListener() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnLoginWithGoogle.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
    }

    private fun alertUserError(message: String) {
        val view = View.inflate(this, R.layout.error_401_dialog, null)

        AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
            .setTitle(message)
            .setView(view)
            .setPositiveButton("Back to Register") { _, _ ->
                //DO NOTHING
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

    @Suppress("DEPRECATION")
    private fun setUpView(){
        //hide the action bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                disableProgressBar()
                finish()
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }
}