package com.example.sandoughentezar.ui.auth

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentValidatePhoneBinding
import com.example.sandoughentezar.ui.MainActivity
import com.example.sandoughentezar.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ValidatePhoneFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentValidatePhoneBinding
    private lateinit var mobile: String
    private lateinit var user_id: String
    private lateinit var code: String
    private val authViewModel by viewModels<AuthViewModel>()
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mobile = it["mobile"].toString()
            user_id = it["user_id"].toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentValidatePhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun setTimer() {
        object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.txtTimer.isEnabled = false
                setTextAnimation(binding.txtTimer, (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                binding.txtTimer.isEnabled = true
                binding.txtTimer.text = "درخواست مجدد"
            }
        }.start()
    }

    private fun setTextAnimation(textView: TextView, message: String) {
        textView.animate().setDuration(300).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                textView.text = message
                textView.animate().setListener(null).setDuration(300).alpha(1f)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).alpha(0f)
    }


    private fun initViews() {
        sharedPreferences = requireActivity().getSharedPreferences("shp", Context.MODE_PRIVATE)
        setTimer()
        binding.txtPhone.text = mobile
        validatePhone()
    }

    private fun validatePhone() {
        authViewModel.validatePhone(getParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    if (it.data!!.status == "ok") {
                        code = it.data.code

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

    private fun getParams(): HashMap<String, String> {
        var hashMap: HashMap<String, String> = HashMap()
        hashMap["user_id"] = user_id
        hashMap["mobile"] = user_id
        return hashMap
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnLogin.id -> {
                if (binding.edtConfirmCode.text.isNotBlank()) {
                    if (binding.edtConfirmCode.text.trim().toString() == code) {
                        sharedPreferences!!.edit().apply() {
                            putString("user_id", user_id).apply()
                        }
                        requireActivity().startActivity(
                            Intent(
                                requireActivity(),
                                MainActivity::class.java
                            )
                        )
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "کد اشتباه است", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "لطفا کد دریافتی را وارد نمایید",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            binding.txtTimer.id -> {
                validatePhone()
            }
        }
    }
}