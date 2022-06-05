package com.example.travelokaocr.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AccessProfileRepository
import com.example.travelokaocr.databinding.FragmentProfileBinding
import com.example.travelokaocr.ui.auth.LoginActivity
import com.example.travelokaocr.ui.profile.EditProfileActivity
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AccessProfileViewModel
import com.example.travelokaocr.viewmodel.factory.AccessProfileFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference

class ProfileFragment : Fragment(), View.OnClickListener {
    //BIDNING
    private var _binding : FragmentProfileBinding? = null

    private val binding get() = _binding!!

    //API
    private lateinit var viewModel: AccessProfileViewModel

    private lateinit var savedPreference: SavedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //SETUP
        itemOnClickListener()

        //CREATE API CONNECTION
        val factory = AccessProfileFactory(AccessProfileRepository())
        viewModel = ViewModelProvider(this, factory)[AccessProfileViewModel::class.java]

        savedPreference = SavedPreference(requireContext())

        val dataUser = savedPreference.getData(Constants.ACCESS_TOKEN)
        showDataUser(dataUser!!)
    }

    private fun showDataUser(dataUser: String){
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

                        val username = result.data?.user?.name.toString()
                        val email = result.data?.user?.email.toString()
                        val fotoProfil = result.data?.user?.foto_profil

                        //SHOW DATA
                        binding.tvUsername.text = username
                        binding.tvEmail.text = email
                        Glide.with(this)
                            .load(fotoProfil)
                            .centerCrop()
                            .into(binding.ivProfilePicture)
                    } else {
                        Log.d("PROFILE", result.status.toString())
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
                activity?.finish()
            }
            R.id.tv_about_traveloka -> {

            }
        }
    }

    private fun progressBar(isLoading: Boolean) = with(binding){
        if (isLoading) {
            this.progressBar.visibility = View.VISIBLE
        } else {
            this.progressBar.visibility = View.GONE
        }
    }
}