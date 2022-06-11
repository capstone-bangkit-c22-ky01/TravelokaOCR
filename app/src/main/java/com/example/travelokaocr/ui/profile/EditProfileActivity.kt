package com.example.travelokaocr.ui.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.databinding.ActivityEditProfileBinding
import com.example.travelokaocr.ui.main.fragment.ProfileFragment
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.utils.uriToFile
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    //BINDING
    private lateinit var binding: ActivityEditProfileBinding

    //API
    private lateinit var authViewModel: AuthViewModel
    private lateinit var viewModel: AccessProfileViewModel

    private lateinit var savedPreference: SavedPreference
    private lateinit var accessToken: String

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]

        val authFactory = AuthViewModelFactory(AuthRepository())
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        savedPreference = SavedPreference(this)

        val token = savedPreference.getData(Constants.ACCESS_TOKEN)
        accessToken = "Bearer $token"

        binding.tvEditProfile.setOnClickListener{
            finish()
        }
        binding.tvUploadImage.setOnClickListener{
            startGallery()
        }
        binding.btnSaveChanges.setOnClickListener{
            update()
            val fragment: Fragment = ProfileFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }

        setUpButton()

        showDataUser()

    }

    //SHOW DATA
    private fun showDataUser(){
        val username = savedPreference.getData(Constants.USERNAME)
        val email = savedPreference.getData(Constants.EMAIL)
        val profilePicture = savedPreference.getData(Constants.PROFILE_PICTURE)

        //SHOW DATA
        binding.edtUsername.setText(username)
        binding.edtEmail.setText(email)

        if (profilePicture == "null"){
            binding.ivProfilePicture.setImageURI(null)
        }else{
            binding.ivProfilePicture.setImageURI(profilePicture?.toUri())
        }

    }

    private fun validateUsername(): String? {
        val name = binding.edtUsername.text.toString()

        if (name.isEmpty()) {
            return getString(R.string.error_empty_message)
        }
        return null
    }

    private fun validateEmail(): String? {
        val email = binding.edtEmail.text.toString()

        if (email.isEmpty()) {
            return getString(R.string.error_empty_message)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.error_invalid_email_message)
        }
        return null
    }

    private fun validateButton() {
        binding.tilUsername.helperText = validateUsername()
        binding.tilEmail.helperText = validateEmail()

        val validUsername = binding.tilUsername.helperText == null
        val validEmail = binding.tilEmail.helperText == null

        val username = binding.edtUsername.text.toString()
        val email = binding.edtEmail.text.toString()

        if (validEmail && validUsername) {
            binding.btnSaveChanges.isEnabled =
                (username.isNotEmpty()) &&
                        (email.isNotEmpty())
        }
    }

    private fun setUpButton() {
        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun afterTextChanged(p0: Editable?) {
//                validateButton()
            }
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun afterTextChanged(p0: Editable?) {
//                validateButton()
            }
        })
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK){
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.ivProfilePicture.setImageURI(selectedImg)

        }
    }

//    private fun uploadImage(name: String, email: String, foto_profil: Url) {
//        if (getFile != null) {
//            val file = reduceFileImage(getFile as File)
//
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                file.name,
//                requestImageFile
//            )
//
//            viewModel.updateUser(name, email, foto_profil).observe(this){response ->
//                if (response is Resources.Loading) {
//                    progressBar(true)
//                } else if (response is Resources.Error) {
//                    progressBar(false)
//                    Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
//                } else if (response is Resources.Success) {
//                    progressBar(false)
//                    val result = response.data
//                    if (result != null) {
//                        if (result.status.equals("success")) {
//
//                            val username = result.data?.user?.name.toString()
//                            val email = result.data?.user?.email.toString()
//                            val fotoProfil = result.data?.user?.foto_profil
//
//                            if (!binding.edtUsername.hint.equals(username)) {
//                                binding.edtUsername.hint = username
//                                return@observe
//                            } else if (!binding.edtEmail.hint.equals(email)) {
//                                binding.edtEmail.hint = email
//                                return@observe
//                            } else if (!binding.ivProfilePicture.equals(fotoProfil)) {
////                                binding.ivProfilePicture = fotoProfil
//                                return@observe
//                            }
//
//                            Toast.makeText(this@EditProfileActivity, "success", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.d("PROFILE", result.status.toString())
//                        }
//                    } else {
//                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

    private fun update() {
        binding.tilUsername.helperText = validateUsername()
        binding.tilEmail.helperText = validateEmail()

        val validUsername = binding.tilUsername.helperText == null
        val validEmail = binding.tilEmail.helperText == null

        val username = binding.edtUsername.text?.toString()?.trim()
        val email = binding.edtEmail.text?.toString()?.trim()

        if(validUsername && validEmail){
            val dataLoginUsername = username?.toRequestBody("text/plain".toMediaType())
            val dataLoginEmail = email?.toRequestBody("text/plain".toMediaType())

            if (getFile != null) {
                //api process edit profile with image
                val requestImageFile = getFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "foto_profile",
                    getFile!!.name,
                    requestImageFile
                )

                observeUpdateProfile(accessToken, dataLoginUsername, dataLoginEmail, imageMultipart)
            } else {
                //api process
                observeUpdateProfile(accessToken, dataLoginUsername, dataLoginEmail, null)
            }
        }
        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
    }

    private fun observeUpdateProfile(accessToken: String, dataUserUsername: RequestBody?, dataUserEmail: RequestBody?, imageMultipart: MultipartBody.Part?) {
        viewModel.updateUser(accessToken, dataUserUsername, dataUserEmail, imageMultipart).observe(this) { response ->
            if (response is Resources.Loading) {
                progressBar(true)
            }
            else if (response is Resources.Error) {
                progressBar(false)
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                progressBar(false)
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {

                        val username = binding.edtUsername.text?.toString()?.trim()
                        val email = binding.edtEmail.text?.toString()?.trim()
                        val photoProfile = result.data?.imageUri

                        savedPreference.putData(Constants.USERNAME, username!!)
                        savedPreference.putData(Constants.EMAIL, email!!)
                        savedPreference.putData(Constants.PROFILE_PICTURE, photoProfile!!)

                    } else {
                        val dataToken = hashMapOf(
                            "refreshToken" to savedPreference.getData(Constants.REFRESH_TOKEN)
                        )

                        if (imageMultipart == null) {
                            observeUpdateTokenProfile(dataToken, dataUserUsername, dataUserEmail, null)
                        } else {
                            observeUpdateTokenProfile(dataToken, dataUserUsername, dataUserEmail, imageMultipart)
                        }

                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeUpdateTokenProfile(dataToken: HashMap<String, String?>, dataUser: RequestBody?, dataEmail: RequestBody?, imageMultipart: MultipartBody.Part? ) {
        authViewModel.updateToken(dataToken).observe(this) { response ->
            if (response is Resources.Loading) {
                progressBar(true)
            }
            else if (response is Resources.Error) {
                progressBar(false)
                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                progressBar(false)
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        val newAccessToken = result.data?.accessToken.toString()
                        //save new token
                        savedPreference.putData(Constants.ACCESS_TOKEN, newAccessToken)

                        //get new token
                        val tokenFromAPI = (savedPreference.getData(Constants.ACCESS_TOKEN))
                        val accessToken = "Bearer $tokenFromAPI"

                        Log.d("NEW ACCESS TOKEN", "observeUpdateToken: $accessToken")

                        if (imageMultipart == null) {
                            observeUpdateProfile(accessToken, dataUser, dataEmail, null)
                        } else {
                            observeUpdateProfile(accessToken, dataUser, dataEmail, imageMultipart)
                        }
                    }
                    else {
                        Log.d("REGIS", result.status.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
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