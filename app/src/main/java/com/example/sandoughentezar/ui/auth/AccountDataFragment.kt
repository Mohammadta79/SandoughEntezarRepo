package com.example.sandoughentezar.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentAccountDataBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody

@AndroidEntryPoint
class AccountDataFragment : Fragment() {
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentAccountDataBinding
    private var name: String? = null
    private var national_id: String? = null
    private var mobile1: String? = null
    private var mobile2: String? = null
    private var address: String? = null
    private var postalCode: String? = null
    private var phone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it["name"].toString()
            national_id = it["national_id"].toString()
            mobile1 = it["mobile1"].toString()
            mobile2 = it["mobile2"].toString()
            address = it["address"].toString()
            postalCode = it["postalCode"].toString()
            phone = it["phone"].toString()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.agreementChb.setOnClickListener {
            if (binding.agreementChb.isChecked) {
                enableRegister(1)
            } else {
                enableRegister(0)
            }
        }
        binding.btnRegister.setOnClickListener {
            register()
        }
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

    private fun checkInput(): Boolean {
        return !(binding.edtAccountNumber.text.isBlank() || binding.edtCardNumber1.text.isBlank() || binding.edtShaba.text.isBlank())
    }

    private fun register() {
        if (!checkInput()) {
            Toast.makeText(
                requireContext(),
                "لطفا تمامی فیلدهای ستاره دار را وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        }
        authViewModel.register(
            getBodyParams()
        ).observe(viewLifecycleOwner) {
            when (it.status) {

                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    binding.btnRegister.isEnabled = true

                    when (it.data!!.statusCode) {
                        200 -> {
                            var bundle = Bundle()
                            bundle.putString("national_id", national_id)
                            findNavController().navigate(
                                R.id.action_accountDataFragment2_to_uploadImageFragment2,
                                bundle
                            )
                        }
                        500 -> {
                            Toast.makeText(
                                requireContext(),
                                "این اطلاعات از قبل ثبت شده است",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        400 -> {
                            Toast.makeText(
                                requireContext(),
                                "خطا در تایید اطلاعات وارد شده",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "خطا در برقراری ارتباط با سرور",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                    binding.btnRegister.isEnabled = false
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "خطا در برقراری ارتباط با سرور",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun getBodyParams(): RequestBody {

        var mobile2: String? = if (mobile2 != null) {
            mobile2
        } else {
            "null"
        }
        var phone: String? = if (phone != null) {
            phone
        } else {
            "null"
        }

        var card2: String? = if (binding.edtCardNumber2.text.isNotBlank()) {
            binding.edtCardNumber2.text.toString()
        } else {
            "null"
        }

        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", name!!)
            .addFormDataPart("national_id", national_id!!)
            .addFormDataPart("mobile1", mobile1!!)
            .addFormDataPart("postal_code", postalCode!!)
            .addFormDataPart("address", address!!)
            .addFormDataPart("account_number", binding.edtAccountNumber.text.toString())
            .addFormDataPart("card1", binding.edtCardNumber1.text.toString())
            .addFormDataPart("shaba", binding.edtShaba.text.toString())
//            .addPart(
//                MultipartBody.Part.createFormData(
//                    "card_image",
//                    card_file!!.name,
//                    RequestBody.create(
//                        "image".toMediaTypeOrNull(),
//                        card_file!!
//                    )
//                )
//            ).addPart(
//                MultipartBody.Part.createFormData(
//                    "passport_image",
//                    passport_file!!.name,
//                    RequestBody.create(
//                        "image".toMediaTypeOrNull(),
//                        passport_file!!
//                    )
//                )
//            )
            .addFormDataPart("mobile2", mobile2!!)
            .addFormDataPart("phone", phone!!)
            .addFormDataPart("card2", card2!!)
            .build()
    }


}




