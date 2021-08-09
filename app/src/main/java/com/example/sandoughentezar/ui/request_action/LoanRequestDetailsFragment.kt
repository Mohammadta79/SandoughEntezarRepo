package com.example.sandoughentezar.ui.request_action

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoanRequestDetailsBinding
import com.example.sandoughentezar.viewModels.RequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanRequestDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLoanRequestDetailsBinding
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    private lateinit var request_id: String
    private lateinit var amount: String
    private lateinit var request_message: String
    private lateinit var installment: String
    private lateinit var date: String
    private val requestViewModel by viewModels<RequestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request_id = it.get("request_id").toString()
            amount = it.get("amount").toString()
            request_message = it.get("request_message").toString()
            installment = it.get("installment").toString()
            date = it.get("date").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoanRequestDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null).let {
            user_id = it.toString()
        }
        setData()
        getReply()

    }

    private fun getRequestReplyParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["request_id"] = request_id
        return params
    }

    private fun setData() {
        binding.txtDate.text = date
        binding.txtRequestAmount.text = amount
        binding.txtRequestInstallment.text = installment
        binding.txtRequestDetails.text = request_message
    }

    private fun getReply() {
        requestViewModel.requestReply(getRequestReplyParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.message!=null) {
                        binding.rootReply.visibility = View.VISIBLE
                        binding.txtNoReply.visibility = View.GONE
                        binding.txtDateReply.text = it.data.date
                        binding.txtTitleReply.text = it.data.title
                        binding.txtMessageReply.text = it.data.message
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


}