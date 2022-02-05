package com.example.sandoughentezar.ui.dashbord_action

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.PaymentRecordsAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentPaymentBinding
import com.example.sandoughentezar.models.PaymentModel
import com.example.sandoughentezar.utils.NumberTextWatcherForThousand
import com.example.sandoughentezar.viewModels.PaymentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wdullaer.materialdatetimepicker.JalaliCalendar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class PaymentFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {
    var dpd: DatePickerDialog? = null
    lateinit var start_date: PersianDate
    lateinit var end_date: PersianDate
    lateinit var pdformater: PersianDateFormat
    var calendarType: DatePickerDialog.Type = DatePickerDialog.Type.JALALI
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var user_id: String
    private val paymentViewModel by viewModels<PaymentViewModel>()
    private var sharedPref: SharedPreferences? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    private lateinit var payment_list: ArrayList<PaymentModel>
    private lateinit var filter_list: ArrayList<PaymentModel>
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
        payment_list = ArrayList()
        filter_list = ArrayList()
        start_date = PersianDate()
        end_date = PersianDate()
        pdformater = PersianDateFormat("Y/m/d")
        setupDatePicker()
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.let {
            user_id = it.getString("user_id", null).toString()
        }
        getPaymentRecords()


    }


    private fun setupDatePicker() {
        val now: Calendar? = when (calendarType) {
            DatePickerDialog.Type.GREGORIAN -> Calendar.getInstance()
            DatePickerDialog.Type.JALALI -> JalaliCalendar.getInstance()
        }
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                calendarType,
                this,
                now!!.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            dpd!!.calendarType = calendarType
            dpd!!.initialize(
                this,
                now!!.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
        }
        when (dpd?.calendarType) {
            DatePickerDialog.Type.GREGORIAN -> dpd!!.setFont(null)
            DatePickerDialog.Type.JALALI -> {
                val typeface = ResourcesCompat.getFont(requireContext(), R.font.kalameh_bold)
                dpd!!.setFont(typeface)
            }
        }
    }

    private fun getPaymentRecords() {
        paymentViewModel.getPaymentRecords(getRecordParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.size == 0) {
                        binding.txtNoPayment.visibility = View.VISIBLE
                    } else {
                        payment_list = it.data
                        binding.txtNoPayment.visibility = View.GONE
                        binding.paymentRecordsRV.apply {
                            adapter = PaymentRecordsAdapter(requireContext(), payment_list)
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnNewPay.id -> {
                setupNewPaymentDialog()
            }
            binding.txtFilterStartDate.id -> {
                dpd!!.show(requireActivity().supportFragmentManager, "start_date")
            }
            binding.txtFilterEndDate.id -> {
                dpd!!.show(requireActivity().supportFragmentManager, "end_date")
            }
            binding.btnFilterPaymentList.id -> {

                filter_list.clear()
                payment_list.forEachIndexed { i, _ ->

                    var date = PersianDate()
                    date.shYear = payment_list[i].date[0]
                    date.shMonth = payment_list[i].date[1]
                    date.shDay = payment_list[i].date[2]
                    if (date.before(start_date) && date.after(end_date)
                    ) {
                        filter_list.add(payment_list[i])
                    }
                }
                if (filter_list.isEmpty()) {
                    binding.txtNoPayment.text = " پرداختی یافت نشد "
                    binding.txtNoPayment.visibility = View.GONE
                } else {
                    binding.paymentRecordsRV.apply {
                        adapter = PaymentRecordsAdapter(requireContext(), filter_list)
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }

            }
        }
    }

    private fun setupNewPaymentDialog() {
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_paymant,
            requireActivity().findViewById<LinearLayout>(R.id.payment_dialog_root)
        )
        val edtNewPayment = bottomSheetView!!.findViewById<EditText>(R.id.edt_amount)
        edtNewPayment.addTextChangedListener(NumberTextWatcherForThousand(edtNewPayment))
        bottomSheetView!!.findViewById<Button>(R.id.btn_dialog_payment).setOnClickListener {
            Payment(
                NumberTextWatcherForThousand.trimCommaOfString(edtNewPayment.text.toString())
                    .toLong()
            )

        }


        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }


    private fun selectedViews() {
        binding.btnNewPay.setOnClickListener(this)
        binding.txtFilterStartDate.setOnClickListener(this)
        binding.txtFilterEndDate.setOnClickListener(this)
        binding.btnFilterPaymentList.setOnClickListener(this)
    }


    private fun Payment(amount: Long) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://sandogh.entezarhoco.ir/api/newpay/?member_id=$user_id&amount=$amount")
        )
        startActivity(browserIntent)
    }


    private fun getRecordParams()
            : HashMap<String, String> {
        var params = HashMap<String, String>()
        params["member_id"] = user_id
        return params
    }

    override fun onDateSet(
        view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int
    ) {
        when (view?.tag) {
            "start_date" -> {

                start_date.shYear = year
                start_date.shMonth = monthOfYear + 1
                start_date.shDay = dayOfMonth
                binding.txtFilterStartDate.text = pdformater.format(start_date)
            }
            "end_date" -> {
                end_date.shYear = year
                end_date.shMonth = monthOfYear + 1
                end_date.shDay = dayOfMonth
                binding.txtFilterEndDate.text = pdformater.format(end_date)
            }
        }
    }
}