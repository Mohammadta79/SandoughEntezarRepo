package com.example.sandoughentezar.ui.auth

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
import com.example.sandoughentezar.databinding.FragmentForgotPasswordBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            if (binding.edtNationalId.text.isNotBlank()) {
                forgotPass()
            }
        }
    }


    private fun forgotPass() {
        authViewModel.forgotPass(getParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    when (it.data!!.status) {
                        "ok" -> {
                            var bundle = Bundle()
                            bundle.putString("user_id", it.data.user_id)
                            findNavController().navigate(
                                R.id.action_forgotPasswordFragment_to_validatePhoneFragment,
                                bundle
                            )
                        }
                        "fail" -> {
                            Toast.makeText(
                                requireContext(),
                                "کد ملی شما در سامانه وجود ندارد، لطف ثبت نام کنید",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        "wait" -> {
                            Toast.makeText(
                                requireContext(),
                                "پروفایل شما هنوز توسط مدیریت تایید نشده است.",
                                Toast.LENGTH_LONG
                            ).show()
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
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["national_id"] = binding.edtNationalId.text.toString()
        return params
    }

}