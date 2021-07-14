package com.example.sandoughentezar.ui.dashbord_action

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.InstallmentAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentDeffearedInstallmentBinding
import com.example.sandoughentezar.interfaces.OnInstallmentClickListener
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.viewModels.DashbordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeffearedInstallmentFragment : Fragment(), OnInstallmentClickListener {


    private lateinit var binding: FragmentDeffearedInstallmentBinding

    private val dashbordViewModel by viewModels<DashbordViewModel>()
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeffearedInstallmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    override fun onInstallmentClick(data: InstallmentModel) {
        installmentPay(data.id)
    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.let {
            user_id = it.getString("user_id", null).toString()
        }
        getDeffearedInstallment()
    }

    private fun getDeffearedInstallment() {
        dashbordViewModel.getDeffearedInstllment(getDeaffearedParams())
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        if (it.data!!.isEmpty()) {
                            binding.txtNoInstalment.visibility = View.VISIBLE
                        } else {
                            binding.txtNoInstalment.visibility = View.INVISIBLE
                            binding.deffearedInstallmentRV.apply {
                                adapter = InstallmentAdapter(
                                    requireContext(),
                                    it.data!!,
                                    this@DeffearedInstallmentFragment
                                )
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                            }
                        }

                    }
                    Status.Failure -> {
                        Log.d("getDeffearedInstallment", it.msg)

                    }
                    Status.Loading -> {
                        //TODO : Show progressbar
                    }
                }
            }
    }

    private fun installmentPay(id: String) {
        dashbordViewModel.installmentPay(getPaymentParams(id)).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    Toast.makeText(requireContext(), "پرداخت با موفقیت انجام شد", Toast.LENGTH_LONG)
                        .show()
                }
                Status.Failure -> {
                    Toast.makeText(requireContext(), "خطا در پرداخت", Toast.LENGTH_LONG).show()
                }
                Status.Loading -> {
                    //TODO : Show progressbar
                }
            }
        }
    }

    private fun getDeaffearedParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["user_id"] = user_id
        return params
    }

    private fun getPaymentParams(id: String): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["installment_id"] = id
        return params
    }

}