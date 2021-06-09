package com.example.sandoughentezar.ui.nav_view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentProfileBinding
import com.example.sandoughentezar.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding
    private val userViewModel by viewModels<UserViewModel>()
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        selectedViews()
    }

    private fun initViews() {
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null).let {
            user_id = it.toString()
        }
        getUserInfo()
    }

    private fun getUserInfo() {
        userViewModel.getUSerInfo(getUserParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    it.data!!.let { user ->
                        setData(
                            user.name,
                            user.national_id,
                            user.mobile,
                            user.mobile2,
                            user.tell_number,
                            user.address,
                            user.postal_code,
                            user.account_number,
                            user.card1,
                            user.card2,
                            user.shaba
                        )
                    }

                }
                Status.Failure -> {

                }
                Status.Loading -> {
                    //TODO : Show progressbar
                }
            }
        }
    }

    private fun setData(
        fullname: String,
        national_id: String,
        mobile: String,
        mobile2: String,
        tel_number: String,
        address: String,
        postal_code: String,
        account_number: String,
        card1: String,
        card2: String,
        shaba: String
    ) {
        binding.edtFullName.setText(fullname)
        binding.edtNationalId.setText(national_id)
        binding.edtMobileNumber1.setText(mobile)
        if (mobile2 != "null") {
            binding.edtMobileNumber2.setText(mobile2)
        }
        if (tel_number != "null") {
            binding.edtPhoneNumber.setText(tel_number)
        }
        binding.edtAddress.setText(address)
        binding.edtPostalCode.setText(postal_code)
        binding.edtAccountNumber.setText(account_number)
        binding.edtCardNumber1.setText(card1)
        if (card2 != "null") {
            binding.edtCardNumber2.setText(card2)
        }
        binding.edtShaba.setText(shaba)


    }

    private fun getUserParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["user_id"] = user_id
        return params
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnUpdate.id -> {
                updateProfile()
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

    private fun updateProfile() {
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
            userViewModel.updateUser(getUserParams()).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        when (it.data!!.status) {
                            "ok" -> {
                                Toast.makeText(
                                    requireContext(),
                                    "اطلاعات جدید با موفقیت ثبت شد",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                Toast.makeText(
                                    requireContext(),
                                    "خطا",
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
                    }
                }
            }
        }
    }

    private fun selectedViews() {
        binding.btnUpdate.setOnClickListener(this)
    }


}