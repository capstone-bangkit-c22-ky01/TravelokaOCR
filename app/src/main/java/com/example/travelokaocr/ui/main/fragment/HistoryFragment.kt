package com.example.travelokaocr.ui.main.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelokaocr.R
import com.example.travelokaocr.data.repository.AuthRepository
import com.example.travelokaocr.data.repository.FlightRepository
import com.example.travelokaocr.databinding.FragmentHistoryBinding
import com.example.travelokaocr.ui.adapter.HistoryAdapter
import com.example.travelokaocr.utils.Constants
import com.example.travelokaocr.utils.Resources
import com.example.travelokaocr.viewmodel.AuthViewModel
import com.example.travelokaocr.viewmodel.FlightViewModel
import com.example.travelokaocr.viewmodel.factory.AuthViewModelFactory
import com.example.travelokaocr.viewmodel.factory.FlightViewModelFactory
import com.example.travelokaocr.viewmodel.preference.SavedPreference

class HistoryFragment : Fragment() {
    //BINDING
    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //SESSION
    private lateinit var savedPref: SavedPreference

    //API
    private lateinit var viewModel: FlightViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var list: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //API
        //CREATE API CONNECTION
        val factory = FlightViewModelFactory(FlightRepository())
        viewModel = ViewModelProvider(this, factory)[FlightViewModel::class.java]
        val authFactory = AuthViewModelFactory(AuthRepository())
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        //SETUP
        savedPref = SavedPreference(requireContext())
        configRecyclerView()

        list.setOnItemClickListener {
            val bundle = Bundle().apply { putString("bookingid", it.id) }
            findNavController().navigate(R.id.action_historyFragment_to_historyDetailActivity, bundle)
        }

        binding.ivDelete.setOnClickListener {
            alertDelete()
        }

        val tokenFromApi = savedPref.getData(Constants.ACCESS_TOKEN)
        val accessToken = "Bearer $tokenFromApi"

        observerHistory(accessToken)
    }

    private fun observerHistory(accessToken: String) {
        viewModel.history(accessToken).observe(viewLifecycleOwner) { response ->
            if (response is Resources.Loading) {
                enableProgressBar()
            }
            else if (response is Resources.Error) {
                disableProgressBar()
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        list.differAsync.submitList(result.data?.bookings)

                        if((result.data?.bookings)!!.isEmpty()){
                            binding.containerLl.visibility = View.VISIBLE
                        }
                    }
                    else {
                        println("Result : ${result.status.toString()}")

                        val tokenFromApi = savedPref.getData(Constants.REFRESH_TOKEN)
                        println("refresh token : $tokenFromApi")

                        val dataToken = hashMapOf(
                            "refreshToken" to tokenFromApi
                        )

                        observeUpdateToken(dataToken)
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeUpdateToken(dataToken: HashMap<String, String?>) {
        authViewModel.updateToken(dataToken).observe(viewLifecycleOwner) { response ->
            if (response is Resources.Loading) {
                enableProgressBar()
            }
            else if (response is Resources.Error) {
                disableProgressBar()
                Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
            }
            else if (response is Resources.Success) {
                disableProgressBar()
                val result = response.data
                if (result != null) {
                    if (result.status.equals("success")) {
                        val newAccessToken = result.data?.accessToken.toString()
                        println("new access token : $newAccessToken")

                        //save new token
                        savedPref.putData(Constants.ACCESS_TOKEN, newAccessToken)

                        //get new token
                        val tokenFromAPI = (savedPref.getData(Constants.ACCESS_TOKEN))
                        println("token from api : $tokenFromAPI")

                        val accessToken = "Bearer $tokenFromAPI"
                        println("access token : $accessToken")

                        observerHistory(accessToken)
                    }
                    else {
                        Log.d("REGIS", result.status.toString())
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun configRecyclerView() {
        list = HistoryAdapter(requireContext())
        binding.rvHistoryTickets.apply {
            adapter = list
            layoutManager =
                if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 2)
                } else {
                    LinearLayoutManager(context)
                }
        }
    }

    private fun alertDelete() {
        val view = View.inflate(context, R.layout.delete_all_dialog, null)

        context?.let {
            AlertDialog.Builder(it, R.style.MyAlertDialogTheme)
                .setView(view)
                .setNegativeButton("No"){ _, _ ->
                    //Do Nothing
                }
                .setPositiveButton("Continue") {_, _ ->
                    //Delete

                    //Do Nothing
                }
                .show()
        }
    }

    private fun enableProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun disableProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.containerLl.visibility = View.INVISIBLE
    }
}