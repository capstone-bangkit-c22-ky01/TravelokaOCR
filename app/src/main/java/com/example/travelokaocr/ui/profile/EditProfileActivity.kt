package com.example.travelokaocr.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.databinding.ActivityEditProfileBinding
import com.example.travelokaocr.ui.main.fragment.ProfileFragment
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.utils.reduceFileImage
import com.example.travelokaocr.utils.uriToFile
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Url
import java.io.File

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    //BINDING
    private lateinit var binding: ActivityEditProfileBinding

    //API
    private lateinit var viewModel: AccessProfileViewModel

    private lateinit var savedPreference: SavedPreference

    private var getFile: File? = null

    private val name = binding.edtUsername.text.toString()
    private val email = binding.edtEmail.text.toString()
    private val foto_profil = binding.ivProfilePicture as Url

//    private val dataUser = hashMapOf(
//        "name" to username,
//        "email" to email,
//        "foto_profil" to fotoProfil
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        itemOnClickListener()

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]

        savedPreference = SavedPreference(this)

        val dataUser = savedPreference.getData(Constants.ACCESS_TOKEN)
        showDataUser(name, email, foto_profil)
    }

    //SHOW DATA
    private fun showDataUser(name: String, email: String, foto_profil: Url){
        viewModel.updateUser(name, email, foto_profil).observe(this){ response ->
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

                        val username = result.data?.user?.name.toString()
                        val email = result.data?.user?.email.toString()
                        val fotoProfil = result.data?.user?.foto_profil

                        binding.edtUsername.hint = username
                        binding.edtEmail.hint = email
                        Glide.with(this)
                                .load(fotoProfil)
                                .centerCrop()
                                .into(binding.ivProfilePicture)
                    } else {
                        Log.d("PROFILE", result.status.toString())
                    }
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //USERNAME CHANGED
//    private fun isUsernameChange(dataUser: HashMap<String, String>) {
//        viewModel.updateUser(dataUser).observe(this){ response ->
//            if (response is Resources.Loading) {
//                progressBar(true)
//            } else if (response is Resources.Error) {
//                progressBar(false)
//                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
//            } else if (response is Resources.Success) {
//                progressBar(false)
//                val result = response.data
//                if (result != null) {
//                    if (result.status.equals("success")) {
//
//                        val username = result.data?.name.toString()
//
//                        if (!binding.edtUsername.hint.equals(username)) {
//                            binding.edtUsername.hint = username
//                            return@observe
//                        }
//                    } else {
//                        Log.d("PROFILE", result.message.toString())
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    //EMAIL CHANGED
//    private fun isEmailChange(dataUser: HashMap<String, String>) {
//        viewModel.updateUser(dataUser).observe(this){ response ->
//            if (response is Resources.Loading) {
//                progressBar(true)
//            } else if (response is Resources.Error) {
//                progressBar(false)
//                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
//            } else if (response is Resources.Success) {
//                progressBar(false)
//                val result = response.data
//                if (result != null) {
//                    if (result.status.equals("success")) {
//
//                        val email = result.data?.email.toString()
//
//                        if (!binding.edtEmail.hint.equals(email)) {
//                            binding.edtEmail.hint = email
//                            return@observe
//                        }
//                    } else {
//                        Log.d("PROFILE", result.message.toString())
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    //PHOTO PROFILE CHANGED
//    private fun isPhotoChange(dataUser: HashMap<String, String>) {
//        viewModel.updateUser(dataUser).observe(this){ response ->
//            if (response is Resources.Loading) {
//                progressBar(true)
//            } else if (response is Resources.Error) {
//                progressBar(false)
//                Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
//            } else if (response is Resources.Success) {
//                progressBar(false)
//                val result = response.data
//                if (result != null) {
//                    if (result.status.equals("success")) {
//
//                        val email = result.data?.email.toString()
//
//                        if (!binding.edtEmail.hint.equals(email)) {
//                            binding.edtEmail.hint = email
//                            return@observe
//                        }
//                    } else {
//                        Log.d("PROFILE", result.message.toString())
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@EditProfileActivity)

            getFile = myFile

            binding.ivProfilePicture.setImageURI(selectedImg)
        }
    }

    private fun uploadImage(name: String, email: String, foto_profil: Url) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.updateUser(name, email, foto_profil).observe(this){response ->
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

                            val username = result.data?.user?.name.toString()
                            val email = result.data?.user?.email.toString()
                            val fotoProfil = result.data?.user?.foto_profil

                            if (!binding.edtUsername.hint.equals(username)) {
                                binding.edtUsername.hint = username
                                return@observe
                            } else if (!binding.edtEmail.hint.equals(email)) {
                                binding.edtEmail.hint = email
                                return@observe
                            } else if (!binding.ivProfilePicture.equals(fotoProfil)) {
//                                binding.ivProfilePicture = fotoProfil
                                return@observe
                            }

                            Toast.makeText(this@EditProfileActivity, "success", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("PROFILE", result.status.toString())
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun update() {
//        isUsernameChange(dataUser)
//        isEmailChange(dataUser)
//        isPhotoChange(dataUser)

        Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
    }

    private fun itemOnClickListener(){
        binding.tvEditProfile.setOnClickListener(this)
        binding.tvUploadImage.setOnClickListener(this)
        binding.btnSaveChanges.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_edit_profile -> {
                startActivity(Intent(this, ProfileFragment::class.java))
            }
            R.id.tv_upload_image -> {
                startGallery()
            }
            R.id.btn_save_changes -> {
                update()
                uploadImage(name, email, foto_profil)
                startActivity(Intent(this, ProfileFragment::class.java))
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