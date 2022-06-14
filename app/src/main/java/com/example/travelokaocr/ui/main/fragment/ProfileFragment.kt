package com.example.travelokaocr.ui.main.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.databinding.FragmentProfileBinding
import com.example.travelokaocr.ui.auth.LoginActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


const val HTTPS_LINK = "https://"
const val URL_LINK = "capstone-bangkit-c22-ky01.github.io/traveloka-ocr-landingpage/"

class ProfileFragment : Fragment(), View.OnClickListener {
    //BINDING
    private var _binding : FragmentProfileBinding? = null

    private val binding get() = _binding!!

    //API
    private lateinit var viewModel: AccessProfileViewModel
    private lateinit var authViewModel: AuthViewModel

    //SESSION
    private lateinit var savedPreference: SavedPreference

    //GOOGLE
    private lateinit var gsc: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SETUP
        itemOnClickListener()

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]

        val authFactory = AuthViewModelFactory(AuthRepository())
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        savedPreference = SavedPreference(requireContext())

        //GOOGLE SIGN IN
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("66670183590-cfunc7u16g4d5n74nhk37mv9cl4garbl.apps.googleusercontent.com")
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(requireActivity(), gso)

        showDataUser()
    }

    private fun showDataUser(){

        val username = savedPreference.getData(Constants.USERNAME)
        val email = savedPreference.getData(Constants.EMAIL)
        val profilePicture = savedPreference.getData(Constants.PROFILE_PICTURE)

        //SHOW DATA
        binding.tvUsername.text = username
        binding.tvEmail.text = email

        Glide.with(requireContext())
            .load(profilePicture)
            .placeholder(R.drawable.avatar)
            .into(binding.ivProfilePicture)

    }

    private fun browserIntent(){
        val url: String = if (!URL_LINK.startsWith(HTTPS_LINK) && !URL_LINK.startsWith(HTTPS_LINK)) {
            HTTPS_LINK + URL_LINK
        } else {
            URL_LINK
        }

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun itemOnClickListener(){
        binding.btnEditProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.tvAboutTraveloka.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_profile -> {
                val toEditProfileFragment = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                binding.root.findNavController().navigate(toEditProfileFragment)
            }
            R.id.btn_logout -> {
                //logout, go to login activity
                alertLogout()
            }
            R.id.tv_about_traveloka -> {
                browserIntent()
            }
        }
    }

    private fun alertLogout() {
        val view = View.inflate(requireContext(), R.layout.profile_logout_dialog, null)

        AlertDialog.Builder(requireContext(), R.style.MyAlertDialogTheme)
            .setTitle("Are you sure want to Logout?")
            .setView(view)
            .setNegativeButton("No"){ _, _ ->
                //DO NOTHING
            }
            .setPositiveButton("Yes") { _, _ ->
                //call api logout
                progressBar(true)
                val refreshToken = savedPreference.getData(Constants.REFRESH_TOKEN)

                //api process
                val dataUpdateToken = hashMapOf(
                    "refreshToken" to refreshToken,
                )

                //logout
                observerLogout(dataUpdateToken)
            }.show()
    }

    private fun observerLogout(dataUpdateToken: HashMap<String, String?>) {
        authViewModel.logoutUser(dataUpdateToken).observe(this) { response ->
            if (response is Resources.Loading) {
                progressBar(true)
            }
            else if (response is Resources.Error) {
                progressBar(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                progressBar(false)
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        //delete session
                        savedPreference.clear()
                        savedPreference.putInstall(Constants.FIRST_INSTALL, false)
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()

                        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
                        if(acct != null){
                            gsc.signOut()
                                .addOnCompleteListener(requireActivity()) {
                                    revokeAccess()
                                }
                        } else {
                            killActivity()
                            startActivity(Intent(requireActivity(), LoginActivity::class.java))
                        }
                    }
                    else {
                        Log.d("REGIS", result.message.toString())
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun revokeAccess() {
        gsc.revokeAccess()
            .addOnCompleteListener(requireActivity()) {
                killActivity()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            }
    }

    private fun killActivity() {
        activity?.finish()
    }

    private fun progressBar(isLoading: Boolean) = with(binding){
        if (isLoading) {
            this.progressBar.visibility = View.VISIBLE
        } else {
            this.progressBar.visibility = View.GONE
        }
    }
}