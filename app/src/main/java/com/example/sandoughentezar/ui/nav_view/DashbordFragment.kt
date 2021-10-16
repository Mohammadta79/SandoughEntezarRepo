package com.example.sandoughentezar.ui.nav_view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.InstallmentAdapter

import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentDashbordBinding
import com.example.sandoughentezar.interfaces.OnInstallmentClickListener
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.viewModels.DashbordViewModel
import com.example.sandoughentezar.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashbordFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentDashbordBinding
    private val dashbordViewModel by viewModels<DashbordViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
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
            binding.cardMyPayments.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_paymentFragment)
            }
            binding.cardMyLoan.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_loanFragment)
            }
            binding.cardDeffearedInstallment.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_deffearedInstallmentFragment)
            }
            binding.cardRules.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_statemetFragment)
            }
            binding.cardMessages.id -> {
                findNavController().navigate(R.id.action_dashbordFragment_to_messageFragment)
            }
            binding.cardMore.id -> {
                requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
                    .openDrawer(Gravity.RIGHT)
            }
        }
    }

    private fun selectedViews() {
        binding.cardMyPayments.setOnClickListener(this)
        binding.cardMyLoan.setOnClickListener(this)
        binding.cardDeffearedInstallment.setOnClickListener(this)
        binding.cardRules.setOnClickListener(this)
        binding.cardMessages.setOnClickListener(this)
        binding.cardMore.setOnClickListener(this)
    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.let {
            user_id = it.getString("user_id", null).toString()
        }

        getMyTotalPay()
        setUserInfo()

    }

//    private fun getMyScore() {
//        dashbordViewModel.getMyScore(getUserParams()).observe(viewLifecycleOwner) {
//            when (it.status) {
//                Status.Success -> {
//                    binding.progressBar.hideProgressBar()
//                    it.data!!.let { _data ->
//                        //     binding.txtSlash.text = "/"
//                        //     binding.txtMyLevel.text = "رتبه" + _data.level
//                        binding.txtMyScore.text = _data.score + "امتیاز"
//                    }
//                }
//                Status.Failure -> {
//                    binding.progressBar.hideProgressBar()
//                }
//                Status.Loading -> {
//                    binding.progressBar.showProgressBar()
//                }
//            }
//        }
//    }

    private fun setUserInfo() {

        userViewModel.getUSerInfo(getUserParams()).observe(viewLifecycleOwner) {

            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()

                    binding.txtNameDashbord.text = it.data!!.name
                    binding.txtId.text =
                        it.data!!.national_id
                    binding.txtAccountNumber.text = it.data.account_number
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    Log.d("setNavHeader", it.msg)
                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()

                }

            }
        }
    }


    private fun getMyTotalPay() {
        dashbordViewModel.getTotalPayment(getUserParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.status == "ok") {
                        var amount = "%,d".format(it.data.amount.toLong())
                        binding.txtMyPayment.text = "$amount تومان "
                    }
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
            }
        }
    }

    //params
    private fun getUserParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["member_id"] = user_id
        return params
    }
}