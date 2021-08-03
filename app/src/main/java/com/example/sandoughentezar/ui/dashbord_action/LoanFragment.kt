package com.example.sandoughentezar.ui.dashbord_action

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.LoanRecordsAdapter
import com.example.sandoughentezar.adapters.PaymentRecordsAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoanBinding
import com.example.sandoughentezar.interfaces.OnLoanItemClickListener
import com.example.sandoughentezar.models.LoanModel
import com.example.sandoughentezar.viewModels.LoanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanFragment : Fragment(), OnLoanItemClickListener {

    private lateinit var binding: FragmentLoanBinding
    private var sharedPref: SharedPreferences? = null
    private val loanViewModel by viewModels<LoanViewModel>()
    private lateinit var user_id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun getLoanRecord() {
        loanViewModel.getLoanRecord(getParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.size == 0) {
                        binding.txtNoLoan.visibility = View.VISIBLE
                    } else {
                        binding.txtNoLoan.visibility = View.GONE
                        binding.loanRV.apply {
                            adapter =
                                LoanRecordsAdapter(requireContext(), it.data!!, this@LoanFragment)
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

    private fun getParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["user_id"] = user_id
        return params
    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null)!!.let {
            user_id = it
        }
        getLoanRecord()

    }

    override fun onLoanItemClick(loan: LoanModel) {
        var bundle = Bundle()
        bundle.putString("id", loan.id)
        bundle.putString("start_date", loan.start_date)
        bundle.putString("end_date", loan.end_date)
        bundle.putString("amount", loan.amount)
        bundle.putString("paid_installment", loan.num_of_paid_installment)
        bundle.putString("unpaid_installment", loan.num_of_unpaid_installment)
        bundle.putString("installment", loan.num_of_installment)
        bundle.putString("due_date", loan.due_date)
        bundle.putString("installment_amount", loan.installment_amount)
        findNavController().navigate(R.id.action_loanFragment_to_loanDetailsFragment, bundle)
    }


}