package com.example.travelokaocr.ui.authentication

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
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.register.RegisterActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    //BINDING
    private lateinit var binding: ActivityLoginBinding

    //API
    private lateinit var viewModel: AuthViewModel
    private lateinit var pref: SavedPreference

    //GOOGLE SIGN IN
    private lateinit var mGoogleSignInClient: GoogleSignInClient

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
        pref = SavedPreference(this)

        //GOOGLE SIGN-IN
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(pref.getSessionLogin(Constants.IS_LOGIN) && account.toString() != "null"){
            startActivity(Intent(this@LoginActivity, FlightActivity::class.java))
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
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
                signIn()
            }
            R.id.sign_up -> {
                //go to sign up activity
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
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

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            viewModel.getGoogleLoginResponse()
            observerLogin()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LOGIN", "signInResult:failed code=" + e.statusCode)
        }
    }

    @Suppress("DEPRECATION")
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setUpButton(){
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

    private fun loginForm() {
        binding.tilEmail.helperText = validateEmail()
        binding.tilPassword.helperText = validatePassword()

        val validEmail = binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null

        val email = binding.etvEmail.text.toString()
        val password = binding.etvPassword.text.toString()

        if (validEmail && validPassword) {
            //api process
            viewModel.getLoginResponse(email, password)
            observerLogin()
        }
    }

    private fun observerLogin() {
        viewModel.login.observe(this){ response ->
            if (response.isSuccessful){
                if(response.body()?.status.equals("success")){
                    val accessToken = response.body()?.data?.accessToken.toString()
                    val refreshToken = response.body()?.data?.refreshToken.toString()
                    saveSession(accessToken, refreshToken)

                    disableProgressBar()
                    Toast.makeText(this, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, FlightActivity::class.java))
                    finish()
                }else{
                    alertUserError(response.body()?.message.toString())
                }
            }else{
                alertServerError(response.code().toString())
            }
        }
    }

    private fun alertServerError(code: String) {
        val view = View.inflate(this, R.layout.error_action_dialog, null)

        AlertDialog.Builder(this)
            .setTitle("ERROR, Code : $code")
            .setView(view)
            .setPositiveButton("Back to Login") { _, _ ->
                //DO NOTHING
            }.show()
    }

    private fun alertUserError(message: String) {
        val view = View.inflate(this, R.layout.error_action_dialog, null)

        AlertDialog.Builder(this)
            .setTitle(message)
            .setView(view)
            .setPositiveButton("Back to Login") { _, _ ->
                //DO NOTHING
            }.show()
    }

    private fun saveSession(accessToken: String, refreshToken: String) {
        pref.putDataLogin(Constants.ACCESS_TOKEN, accessToken)
        pref.putDataLogin(Constants.REFRESH_TOKEN, refreshToken)
        pref.putSessionLogin(Constants.IS_LOGIN, true)
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

    private fun itemOnClickListener() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnLoginWithGoogle.setOnClickListener(this)
        binding.signUp.setOnClickListener(this)
    }

    private fun enableProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun disableProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    companion object{
        const val RC_SIGN_IN = 0
    }
}