package com.example.sandoughentezar.ui.dashbord_action

import android.content.Intent
import android.graphics.drawable.shapes.PathShape
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.InstallmentAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoanDetailsBinding
import com.example.sandoughentezar.interfaces.OnInstallmentClickListener
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.viewModels.DashbordViewModel
import com.example.sandoughentezar.viewModels.LoanViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanDetailsFragment : Fragment(), OnInstallmentClickListener {

    private lateinit var binding: FragmentLoanDetailsBinding
    private lateinit var id: String
    private lateinit var start_date: String
    private lateinit var end_date: String
    private lateinit var amount: String
    private lateinit var paid_installment: String
    private lateinit var unpaid_installment: String
    private lateinit var num_of_installment: String
    private lateinit var due_date: String
    private lateinit var installment_amount: String
    private lateinit var loan_reminder: String
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    private val loanViewModel by viewModels<LoanViewModel>()
    private val dashbordViewModel by viewModels<DashbordViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it["id"].toString()
            start_date = it["start_date"].toString()
            end_date = it["end_date"].toString()
            amount = it["amount"].toString()
            paid_installment = it["paid_installment"].toString()
            unpaid_installment = it["unpaid_installment"].toString()
            num_of_installment = it["installment"].toString()
            due_date = it["due_date"].toString()
            installment_amount = it["installment_amount"].toString()

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        loan_reminder =
            (amount.toLong() - (installment_amount.toLong() * paid_installment.toLong())).toString()
        setTxtData()
        getLoanInstallment()
        binding.txtMoreDetails.setOnClickListener {
            setupDetailsDialog()
        }
    }

    private fun setTxtData() {
        binding.txtAmount.text = amount
        binding.txtStartDate.text = start_date
    }

    private fun setupDetailsDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_loan_details,
            requireActivity().findViewById<ConstraintLayout>(R.id.root_loan_details)
        )
        bottomSheetView!!.findViewById<TextView>(R.id.txt_end_date).text = end_date
        bottomSheetView!!.findViewById<TextView>(R.id.txt_installment_amount).text =
            installment_amount
        bottomSheetView!!.findViewById<TextView>(R.id.txt_num_of_installment).text =
            num_of_installment
        bottomSheetView!!.findViewById<TextView>(R.id.txt_num_of_installments_paid).text =
            paid_installment
        bottomSheetView!!.findViewById<TextView>(R.id.txt_unpaid_installment).text =
            unpaid_installment
        bottomSheetView!!.findViewById<TextView>(R.id.txt_reminder_loan).text = loan_reminder
        bottomSheetView!!.findViewById<TextView>(R.id.due_date).text = due_date
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }


    private fun getLoanInstallment() {
        loanViewModel.getLoanInstallment(getLoanParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.loanInstallmentRV.apply {
                        adapter = InstallmentAdapter(
                            requireContext(),
                            it.data!!,
                            this@LoanDetailsFragment
                        )
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
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

    override fun onInstallmentClick(data: InstallmentModel) {
        installmentPay(data.id)
    }

    private fun installmentPay(id: String) {

          val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.3:8080/api/installmentpay/?installment_id=$id"))
                startActivity(browserIntent)
//        dashbordViewModel.installmentPay(getPaymentParams(id)).observe(viewLifecycleOwner) {
//            when (it.status) {
//                Status.Success -> {
//                    Toast.makeText(requireContext(), "پرداخت با موفقیت انجام شد", Toast.LENGTH_LONG)
//                        .show()
//                }
//                Status.Failure -> {
//                    Toast.makeText(requireContext(), "خطا در پرداخت", Toast.LENGTH_LONG).show()
//                }
//                Status.Loading -> {
//                    //TODO : Show progressbar
//                }
//            }
//        }
    }

    private fun getLoanParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["loan_id"] = id
        return params
    }

    private fun getPaymentParams(id: String): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["installment_id"] = id
        return params
    }

}