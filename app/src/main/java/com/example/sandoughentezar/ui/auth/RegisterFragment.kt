package com.example.sandoughentezar.ui.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentRegisterBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel by viewModels<AuthViewModel>()
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedViews()

    }

    private fun setupWaitDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_wait_for_accept,
            requireActivity().findViewById<LinearLayout>(R.id.root_wait)
        )
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }

    private fun register() {
        if (!checkInput()) {
            Toast.makeText(
                requireContext(),
                "لطفا فیلد های ستاره دار را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidNationalID()) {
            Toast.makeText(
                requireContext(),
                "لطفا کد ملی 10 رقمی خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidMobile(binding.edtMobileNumber1.text.toString())) {
            Toast.makeText(
                requireContext(),
                "لطفا شماره موبایل خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidPostalCode()) {
            Toast.makeText(
                requireContext(),
                "لطفا کد پستی 10 رقمی خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            authViewModel.register(getParams()).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        when (it.data!!.status) {
                            "ok" -> {
                                setupWaitDialog()
                            }
                            "exist" -> {
                                Toast.makeText(
                                    requireContext(),
                                    "این اطلاعات از قبل ثبت شده است",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    requireContext(),
                                    it.data!!.status,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    }
                    Status.Loading -> {
                        //TODO:Show progressbar
                    }
                    Status.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "خطا",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("registerrr", it.data.toString())
                    }
                }
            }
        }

    }


    private fun checkInput(): Boolean {

        return !(binding.edtFullName.text.isBlank() || binding.edtFullName.text.isBlank() || binding.edtMobileNumber1.text.isBlank()
                || binding.edtPostalCode.text.isBlank() || binding.edtAddress.text.isBlank() || binding.edtAccountNumber.text.isBlank()
                || binding.edtCardNumber1.text.isBlank() || binding.edtShaba.text.isBlank())
    }

    private fun isValidNationalID(): Boolean {
        return binding.edtNationalId.text.trim().length == 10
    }

    private fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

    private fun isValidPostalCode(): Boolean {
        return binding.edtPostalCode.text.trim().length == 10
    }


    private fun getParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["name"] = binding.edtFullName.text.toString()
        params["national_id"] = binding.edtNationalId.text.toString()
        params["mobile1"] = binding.edtMobileNumber1.text.toString()
        if (binding.edtMobileNumber2.text.isNotBlank()) {
            params["mobile2"] = binding.edtMobileNumber2.text.toString()
        }
        if (binding.edtPhoneNumber.text.isNotBlank()) {
            params["phone"] = binding.edtPhoneNumber.text.toString()
        }
        params["postal_code"] = binding.edtPostalCode.text.toString()
        params["address"] = binding.edtAddress.text.toString()
        params["account_number"] = binding.edtAccountNumber.text.toString()
        params["card1"] = binding.edtCardNumber1.text.toString()
        if (binding.edtCardNumber2.text.isNotBlank()) {
            params["card2"] = binding.edtCardNumber2.text.toString()
        }
        params["shaba"] = binding.edtShaba.text.toString()
        return params
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnRegister.id -> {
                register()
            }
            binding.agreementChb.id -> {
                if (binding.agreementChb.isChecked) {
                    enableRegister(1)
                } else {
                    enableRegister(0)
                }
            }
        }
    }

    private fun selectedViews() {
        binding.btnRegister.setOnClickListener(this)
        binding.agreementChb.setOnClickListener(this)
    }

    private fun enableRegister(state: Int) {
        when (state) {
            0 -> {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.setBackgroundResource(R.drawable.shape_btn_login_disable)
            }
            1 -> {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.setBackgroundResource(R.drawable.shape_btn_login_enable)
            }
        }
    }

}