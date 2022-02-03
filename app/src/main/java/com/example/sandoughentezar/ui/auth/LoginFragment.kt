package com.example.sandoughentezar.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentLoginBinding
import com.example.sandoughentezar.ui.MainActivity
import com.example.sandoughentezar.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.FormBody
import okhttp3.RequestBody
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentLoginBinding
    private var sharedPreferences: SharedPreferences? = null

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


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
        setupBiometic()
        changeTextColor(view)
        selectViews()
        sharedPreferences = requireActivity().getSharedPreferences("shp", Context.MODE_PRIVATE)
        binding.txtAboutApp.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://topkook.ir/")
            )
            startActivity(browserIntent)
        }
        binding.btnFingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
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
                    when (it.data!!.status) {
                        "ok" -> {
                            binding.progressBar.hideProgressBar()
                            sharedPreferences!!.edit().apply {
                                putString("user_id", it.data.member_id).apply()
                            }
                            requireActivity().startActivity(
                                Intent(
                                    requireActivity(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }
                        "waiting" -> {
                            binding.progressBar.hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "اطلاعات شما هنوز توسط مدیر تایید نشده است",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "fail" -> {
                            binding.progressBar.hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "کد ملی شما موجود نمی باشد",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "wrongpass" -> {
                            binding.progressBar.hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "نام کاربری یا رمز عبور اشتباه است",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            binding.progressBar.hideProgressBar()
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

                }
            }

        }
    }

    private fun setupBiometic() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    binding.edtNationalId.setText(
                        sharedPreferences!!.getString("login_username", null)
                    )
                    binding.edtPassword.setText(
                        sharedPreferences!!.getString(
                            "login_password",
                            null
                        )
                    )
                    login()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("سنسور اثر انگشت")
            .setSubtitle("با استفاده از سنسور اثر انگشت وارد شوید")
            .setNegativeButtonText("بستن")
            .build()

    }

    private fun changeTextColor(view: View) {
        val first = " طراحی و توسعه توسط "
        val last = "<font color='#0032CC'>تاپ کوک</font>"
        view.findViewById<TextView>(R.id.txt_about_app).text = Html.fromHtml(first + last)
    }
}