package com.example.sandoughentezar.ui.nav_view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentSettingsBinding
import com.example.sandoughentezar.ui.MainActivity
import com.example.sandoughentezar.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBiometic()
        sharedPreferences = requireActivity().getSharedPreferences("shp", Context.MODE_PRIVATE)
        binding.txtFingerPrint.setOnClickListener {
            animateFingerCard()
        }
        binding.btnFingerprint.setOnClickListener {
            if (checkInput()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Toast.makeText(
                    requireContext(),
                    "???????? ???????????? ???? ???? ?????? ???? ????????",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun checkInput(): Boolean =
        !(binding.edtNationalId.text.toString()
            .isBlank() || binding.edtPassword.text.toString().isBlank())

    private fun animateFingerCard() {
        if (binding.cardFinger.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(
                binding.rootFinger,
                AutoTransition()
            )
            binding.cardFinger.visibility = View.GONE
        } else {
            TransitionManager.beginDelayedTransition(
                binding.rootFinger,
                AutoTransition()
            )
            binding.cardFinger.visibility = View.VISIBLE
        }
    }

    private fun getParams(): HashMap<String, String> {
        var map = HashMap<String, String>()
        map["username"] = binding.edtNationalId.text.toString()
        map["password"] = binding.edtPassword.text.toString()
        return map
    }

    private fun login() {
        authViewModel.login(
            getParams()
        ).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    when (it.data!!.status) {
                        "ok" -> {
                            sharedPreferences!!.edit().apply {
                                putString(
                                    "login_username",
                                    binding.edtNationalId.text.toString()
                                )
                                putString("login_password", binding.edtPassword.text.toString())

                            }.apply()
                            Toast.makeText(
                                requireContext(),
                                "?????? ?????????? ?????? ???? ???????????? ?????????? ????",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "waiting" -> {
                            Toast.makeText(
                                requireContext(),
                                "?????????????? ?????? ???????? ???????? ???????? ?????????? ???????? ??????",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "fail" -> {
                            Toast.makeText(
                                requireContext(),
                                "???? ?????? ?????? ?????????? ?????? ????????",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        "wrongpass" -> {
                            binding.progressBar.hideProgressBar()
                            Toast.makeText(
                                requireContext(),
                                "?????? ???????????? ???? ?????? ???????? ???????????? ??????",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "?????? ???? ?????????????? ???????????? ???? ????????",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("loginnnn", it.status.toString())
                        }
                    }


                }
                Status.Loading -> {
                }
                Status.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "?????? ???? ?????????????? ???????????? ???? ????????",
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
                    login()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("?????????? ?????? ??????????")
            .setSubtitle("???? ?????????????? ???? ?????????? ?????? ?????????? ?????????????? ?????? ???? ?????? ????????")
            .setNegativeButtonText("????????")
            .build()

    }


}