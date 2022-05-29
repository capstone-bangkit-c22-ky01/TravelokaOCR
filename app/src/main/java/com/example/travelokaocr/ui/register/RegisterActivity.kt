package com.example.travelokaocr.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.travelokaocr.R
import com.example.travelokaocr.data.api.ApiService
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.databinding.ActivityRegisterBinding
import com.example.travelokaocr.ui.login.LoginActivity
import com.example.travelokaocr.viewmodel.AuthenticationViewModel
import com.example.travelokaocr.viewmodel.factory.AuthenticationViewModelFactory
import com.example.travelokaocr.viewmodel.preferences.UserPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


const val RC_SIGN_IN = 123

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var apiService: ApiService
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authenticationViewModelFactory = AuthenticationViewModelFactory(AuthRepository())
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
                binding.progressBar.visibility = View.VISIBLE
                registerForm()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btn_login_with_google -> {
                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

                // Build a GoogleSignInClient with the options specified by gso.
                val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//                val acct = GoogleSignIn.getLastSignedInAccount(this)
//                if (acct != null) {
//                    viewModel.loginGoogleUsers.observe(this) { response ->
//                        if (response.isSuccessful){
//                            if(response.body()?.status.equals("success")){
////                                val id = acct.id
//                                var email = acct.email
//                                var name = acct.displayName
////                                val photo: Uri? = acct.photoUrl
//
//                                email = response.body()?.data?.profile?.email.toString()
//                                name = response.body()?.data?.profile?.name.toString()
//
//                                saveLoginGoogleSession(email, name)
//                            }
//                        }
//                    }
//
////                    val personName = acct.displayName
////                    val personGivenName = acct.givenName
////                    val personFamilyName = acct.familyName
////                    val personEmail = acct.email
////                    val personId = acct.id
////                    val personPhoto: Uri? = acct.photoUrl
//                }

                val signInIntent: Intent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
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
                    binding.progressBar.visibility = View.INVISIBLE
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    invalidRegisterForm()
                }
            } else {
                val view = View.inflate(this, R.layout.error_action_dialog_login_server, null)

                AlertDialog.Builder(this)
                    .setView(view)
                    .setPositiveButton("Back to Registration, Code : ${response.code()}") { _, _ ->
                        //DO NOTHING
                    }.show()
            }
        }
    }

    private fun invalidRegisterForm() {
        val view = View.inflate(this, R.layout.error_action_dialog_register, null)

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Back to Register") { _, _ ->
                //DO NOTHING
            }.show()
    }

//    private fun saveLoginGoogleSession(email: String, name: String){
//        userPreference.putDataGoogleLogin(Constants.EMAIL, email)
//        userPreference.putDataGoogleLogin(Constants.NAME, name)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
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
            Log.w("Google Auth", "signInResult:failed code=" + e.statusCode)
        }
    }
}
