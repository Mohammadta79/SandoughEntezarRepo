package com.example.sandoughentezar.ui.auth

import android.os.Bundle
import android.util.Log
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
import okhttp3.FormBody
import okhttp3.RequestBody

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
        selectViews()
    }

    private fun selectViews() {
        binding.btnLogin.setOnClickListener(this)
        binding.txtForgotPass.setOnClickListener(this)
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
            binding.txtForgotPass.id -> {
                findNavController().navigate(R.id.action_authFragment_to_forgotPasswordFragment)
            }
        }
    }

    private fun getParams(): HashMap<String, String> {
        var map = HashMap<String, String>()
        map["username"] = binding.edtNationalId.text.toString()
        map["password"] = binding.edtPassword.text.toString()
        return map
    }

    private fun checkInput(): Boolean =
        !(binding.edtNationalId.text.toString()
            .isBlank() || binding.edtPassword.text.toString().isBlank())

    private fun login() {
        binding.progressBar.showProgressBar()
        authViewModel.login(
            getParams()
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    when (it.data!!.status) {
                        "ok" -> {
                            var bundle = Bundle()
                            bundle.putString("user_id", it.data.member_id)
                            bundle.putString("mobile", it.data.mobile)
                            findNavController().navigate(
                                R.id.action_authFragment_to_validatePhoneFragment2,
                                bundle
                            )
                        }
                        "waiting" -> {
                            Toast.makeText(
                                requireContext(),
                                "اطلاعات شما هنوز توسط مدیر تایید نشده است",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "fail" -> {
                            Toast.makeText(
                                requireContext(),
                                "کد ملی شما موجود نمی باشد",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "wrongpass" -> {
                            Toast.makeText(
                                requireContext(),
                                "نام کاربری یا رمز عبور اشتباه است",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "خطا در برقراری ارتباط با سرور",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("loginnnn", it.status.toString())
                        }
                    }


                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        "خطا در برقراری ارتباط با سرور",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("loginnnner", it.data.toString())

                }
            }

        }
    }
}