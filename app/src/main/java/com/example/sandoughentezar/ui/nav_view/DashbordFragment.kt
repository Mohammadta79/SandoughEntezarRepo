package com.example.sandoughentezar.ui.nav_view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentDashbordBinding
import com.example.sandoughentezar.viewModels.DashbordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashbordFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentDashbordBinding
    private val dashbordViewModel by viewModels<DashbordViewModel>()
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashbordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedViews()
        initViews()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.morePay.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_paymentFragment)
            }
            binding.txtMoreInstallment.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_loanFragment)
            }
        }
    }

    private fun selectedViews() {
        binding.morePay.setOnClickListener(this)
        binding.txtMoreInstallment.setOnClickListener(this)
    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.let {
            user_id = it.getString("user_id", null).toString()
        }
        getMyScore()

    }

    fun getMyScore() {
        dashbordViewModel.getMyScore(getParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    it.data!!.let { _data ->
                        binding.txtMyLevel.text = _data.level
                        binding.txtMyScore.text = _data.score
                    }
                }
                Status.Failure -> {

                }
                Status.Loading -> {
                    //TODO:Show progressbar
                }
            }
        }
    }

    fun getParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["user_id"] = user_id
        return params
    }
}