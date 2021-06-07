package com.example.sandoughentezar.ui.auth

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
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoginBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        selectViews()
    }

    private fun selectViews() {
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnLogin.id -> {
                if (checkInput()) {

                    login()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "لطفا تمامی فیلد ها را وارد نمایید",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getParams(): HashMap<String, String> {
        var params: HashMap<String, String> = HashMap()
        params["username"] = binding.edtNationalId.text.toString()
        params["password"] = binding.edtPassword.text.toString()
        return params
    }

    private fun checkInput(): Boolean =
        !(binding.edtNationalId.text.toString()
            .isBlank() || binding.edtPassword.text.toString().isBlank())

    private fun initViews() {
    }

    private fun login() {
        authViewModel.login(getParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    if (it.data!!.status == "ok") {
                        var bundle= Bundle()
                        bundle.putString("user_id",it.data.user_id)
                        bundle.putString("mobile",it.data.mobile)
                        findNavController().navigate(R.id.action_authFragment_to_validatePhoneFragment2,bundle)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "نام کاربری یا رمز عبور اشتباه است",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                Status.Loading -> {
                    //TODO:Set Progressbar
                }
                Status.Failure -> {
                    Toast.makeText(requireContext(), "خطا", Toast.LENGTH_SHORT).show()

                }
            }

        }
    }
}