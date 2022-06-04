package com.example.travelokaocr.ui.main.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.databinding.ActivityProfileBinding
import com.example.travelokaocr.ui.auth.LoginActivity
import com.example.travelokaocr.ui.flightscreen.FlightActivity
import com.example.travelokaocr.ui.historyscreen.HistoryActivity
import com.example.travelokaocr.ui.profile.EditProfileActivity
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory

class ProfileFragment : Fragment(), View.OnClickListener {
    //BIDNING
    private lateinit var binding : ActivityProfileBinding

    //API
    private lateinit var viewModel: AccessProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SETUP
        itemOnClickListener()

        val username = binding.tvUsername.text.toString()
        val email = binding.tvEmail.text.toString()
        val fotoProfil = binding.ivProfilePicture.toString()

        val dataUser = hashMapOf(
            "name" to username,
            "email" to email,
            "foto_profil" to fotoProfil
        )
        showDataUser(dataUser)

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]
    }

    private fun showDataUser(dataUser: HashMap<String, String>){
        viewModel.profileUser(dataUser).observe(viewLifecycleOwner){ response ->
            if (response is Resources.Loading) {
                progressBar(true)
            } else if (response is Resources.Error) {
                progressBar(false)
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
            } else if (response is Resources.Success) {
                progressBar(false)
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {

                        val username = result.data?.name.toString()
                        val email = result.data?.email.toString()
                        val fotoProfil = result.data?.foto_profil.toString()

                        //SHOW DATA
                        binding.tvUsername.text = username
                        binding.tvEmail.text = email
                        Glide.with(this)
                            .load(fotoProfil)
                            .centerCrop()
                            .into(binding.ivProfilePicture)
                    } else {
                        Log.d("PROFILE", result.message.toString())
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun itemOnClickListener(){
        binding.btnEditProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.tvAboutTraveloka.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_profile -> {
                startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
            }
            R.id.btn_logout -> {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
//                finish()
            }
            R.id.tv_about_traveloka -> {

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