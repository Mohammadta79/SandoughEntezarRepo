package com.example.sandoughentezar.ui.dashbord_action

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.PaymentRecordsAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentPaymentBinding
import com.example.sandoughentezar.viewModels.PaymentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var user_id: String
    private val paymentViewModel by viewModels<PaymentViewModel>()
    private var sharedPref: SharedPreferences? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        selectedViews()
    }


    private fun initViews() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.let {
            user_id = it.getString("user_id", null).toString()
        }
        getPaymentRecords()

    }

    private fun getPaymentRecords() {
        paymentViewModel.getPaymentRecords(getRecordParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    if (it.data!!.size == 0) {
                        binding.txtNoPayment.visibility = View.VISIBLE
                    } else {
                        binding.txtNoPayment.visibility = View.GONE
                        binding.paymentRecordsRV.apply {
                            adapter = PaymentRecordsAdapter(requireContext(), it.data!!)
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }

                }
                Status.Failure -> {
                    Toast.makeText(requireContext(), "خطا", Toast.LENGTH_SHORT).show()
                }
                Status.Loading -> {
                    //TODO:Show progressbar
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnNewPay.id -> {
                setupNewPaymentDialog()
            }
        }
    }

    private fun setupNewPaymentDialog() {
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_paymant,
            requireActivity().findViewById<LinearLayout>(R.id.payment_dialog_root)
        )
        bottomSheetView!!.findViewById<Button>(R.id.btn_dialog_payment).setOnClickListener {
            newPayment(bottomSheetView!!.findViewById<EditText>(R.id.edt_amount).text.toString())
        }
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }


    private fun selectedViews() {
        binding.btnNewPay.setOnClickListener(this)
    }

    private fun newPayment(amount: String) {
        paymentViewModel.newPayment(getNewPaymentParam(amount)).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    if (it.data!!.status == "ok") {
                        Toast.makeText(
                            requireContext(),
                            "پرداخت با موفقیت انجام شد",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                Status.Failure -> {
                    Toast.makeText(requireContext(), "خطای پرداخت", Toast.LENGTH_SHORT).show()
                }
                Status.Loading -> {
                    //TODO:Show progressbar
                }
            }
        }
    }


    private fun getRecordParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["user_id"] = user_id
        return params
    }

    private fun getNewPaymentParam(amount: String): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["user_id"] = user_id
        params["amount"] = amount
        return params
    }
}