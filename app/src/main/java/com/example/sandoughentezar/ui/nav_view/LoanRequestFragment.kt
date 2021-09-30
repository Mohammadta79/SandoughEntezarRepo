package com.example.sandoughentezar.ui.nav_view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.MessageAdapter
import com.example.sandoughentezar.adapters.RequestAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoanRequestBinding
import com.example.sandoughentezar.interfaces.OnRequestItemClickListener
import com.example.sandoughentezar.models.RequestModel
import com.example.sandoughentezar.ui.dashbord_action.LoanFragmentDirections
import com.example.sandoughentezar.viewModels.RequestViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanRequestFragment : Fragment(), View.OnClickListener, OnRequestItemClickListener {

    private lateinit var binding: FragmentLoanRequestBinding
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    private val requestViewModel by viewModels<RequestViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoanRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        selectedViews()
        getRequests()

    }

    private fun setupNewRequestDialog() {
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_new_request,
            requireActivity().findViewById<LinearLayout>(R.id.root_dialog_new_request)
        )
        bottomSheetView!!.findViewById<EditText>(R.id.edt_request_installment)
            .addTextChangedListener(object :
                TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    try {
                        if (s.toString() == "0"){
                            bottomSheetView!!.findViewById<TextView>(R.id.txt_installment_request).text = "نا معتبر"
                        }
                        else{
                            var amount =
                                bottomSheetView!!.findViewById<EditText>(R.id.edt_request_amount).text.toString()
                            var installment = ((amount.toDouble())/(s.toString().toDouble()))
                            var ins =  "%,d".format(installment.toLong())
                            bottomSheetView!!.findViewById<TextView>(R.id.txt_installment_request).text =
                                "$ins تومان "
                        }


                    } catch (e: Exception) {

                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        
        bottomSheetView!!.findViewById<Button>(R.id.btn_create_request).setOnClickListener {
            var amount =
                bottomSheetView!!.findViewById<EditText>(R.id.edt_request_amount).text.toString()
            var installment =
                bottomSheetView!!.findViewById<EditText>(R.id.edt_request_installment).text.toString()
            var details =
                bottomSheetView!!.findViewById<EditText>(R.id.edt_request_details).text.toString()

            newRequest(amount, installment, details)
        }
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }

    private fun initViews() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null).let {
            user_id = it.toString()
        }

    }

    private fun newRequest(amount: String, installment: String, details: String) {
        requestViewModel.newRequest(getNewRequestParams(details, amount, installment))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        binding.progressBar.hideProgressBar()
                        if (it.data!!.status == "ok") {
                            Toast.makeText(
                                requireContext(),
                                "درخواست با موفقیت ایجاد شد",
                                Toast.LENGTH_SHORT
                            ).show()
                            bottomSheetDialog!!.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "خطا در ایجاد درخواست",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Status.Failure -> {
                        binding.progressBar.hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "خطا در برقراری ارتباط با سرور",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.Loading -> {
                        binding.progressBar.showProgressBar()
                    }
                }

            }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnNewRequest.id -> {
                setupNewRequestDialog()
            }
        }
    }

    private fun selectedViews() {
        binding.btnNewRequest.setOnClickListener(this)
    }

    private fun getRequests() {
        requestViewModel.getRequests(getRequestParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.size == 0) {
                        binding.txtNoRequest.visibility = View.VISIBLE
                    } else {
                        binding.txtNoRequest.visibility = View.INVISIBLE
                        binding.requestsRV.apply {
                            adapter =
                                RequestAdapter(requireContext(), it.data, this@LoanRequestFragment)
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        "خطا در برقراری ارتباط با سرور",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
            }
        }
    }

    private fun getNewRequestParams(
        details: String,
        amount: String,
        installment: String
    ): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["user_id"] = user_id
        params["details"] = details
        params["amount"] = amount
        params["num_of_installments"] = installment
        return params
    }

    private fun getRequestParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["user_id"] = "1"
        return params
    }

    override fun onRequestClick(data: RequestModel) {
        var bundle = Bundle()
        bundle.putString("request_id", data.id)
        bundle.putString("amount", data.amount)
        bundle.putString("request_message", data.details)
        bundle.putString("installment", data.num_of_installments)
        bundle.putString("date", data.date)
        findNavController().navigate(
            R.id.action_loanRequestFragment_to_loanRequestDetailsFragment,
            bundle
        )
    }

}