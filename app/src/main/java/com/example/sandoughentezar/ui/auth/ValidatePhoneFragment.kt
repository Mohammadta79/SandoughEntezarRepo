package com.example.sandoughentezar.ui.auth

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    private lateinit var user_id: String
    private lateinit var code: String
    private val authViewModel by viewModels<AuthViewModel>()
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
        validatePhone()
        selectedViews()
        binding.edtVerifyCodeFragmentEdt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    binding.edtVerifyCodeFragmentEdt1.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_filled
                    )
                    binding.edtVerifyCodeFragmentEdt2.requestFocus()
                } else if (count == 0) {
                    binding.edtVerifyCodeFragmentEdt1.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_gray
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtVerifyCodeFragmentEdt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    binding.edtVerifyCodeFragmentEdt2.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_filled
                    )
                    binding.edtVerifyCodeFragmentEdt3.requestFocus()
                } else if (count == 0) {
                    binding.edtVerifyCodeFragmentEdt2.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_gray
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtVerifyCodeFragmentEdt3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    binding.edtVerifyCodeFragmentEdt3.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_filled
                    )
                    binding.edtVerifyCodeFragmentEdt4.requestFocus()
                } else if (count == 0) {
                    binding.edtVerifyCodeFragmentEdt3.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_gray
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtVerifyCodeFragmentEdt4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    binding.edtVerifyCodeFragmentEdt4.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_filled
                    )
                    binding.root.clearFocus()
                    hideKeyboard(requireActivity())
                } else if (count == 0) {
                    binding.edtVerifyCodeFragmentEdt4.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.shape_verifycode_textview_gray
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
                    Toast.makeText(
                        requireContext(),
                        "خطا در برقراری ارتباط با سرور",
                        Toast.LENGTH_SHORT
                    ).show()
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
        return hashMap
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnLogin.id -> {
                if (binding.edtVerifyCodeFragmentEdt1.text.isNotBlank() && binding.edtVerifyCodeFragmentEdt2.text.isNotBlank() &&
                    binding.edtVerifyCodeFragmentEdt3.text.isNotBlank() && binding.edtVerifyCodeFragmentEdt4.text.isNotBlank()
                ) {
                    var user_code =
                        (binding.edtVerifyCodeFragmentEdt1.text.toString() + binding.edtVerifyCodeFragmentEdt2.text.toString()
                                + binding.edtVerifyCodeFragmentEdt3.text.toString() + binding.edtVerifyCodeFragmentEdt4.text.toString())
                    if (user_code == code) {
                        sharedPreferences!!.edit().apply {
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

    private fun selectedViews() {
        binding.btnLogin.setOnClickListener(this)
    }
}