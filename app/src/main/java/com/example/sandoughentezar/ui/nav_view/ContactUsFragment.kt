package com.example.sandoughentezar.ui.nav_view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentContactUsBinding
import com.example.sandoughentezar.viewModels.CompanyDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel


@AndroidEntryPoint
class ContactUsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentContactUsBinding
    private var mobile1: String? = null
    private var mobile2: String? = null
    private var mobile3: String? = null
    private var email1: String? = null
    private var email2: String? = null
    private var email3: String? = null
    private val viewModel by viewModels<CompanyDetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDetails()
        selectedViews()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.contactUsEmail1.id -> {
                try {
                    sendEmail(email1!!)
                }catch (e:Exception){

                }
            }
            binding.contactUsMobile1.id -> {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    try {
                        callMobileNumber(mobile1!!)
                    } catch (e: Exception) {

                    }

                }
            }
            binding.contactUsEmail2.id -> {
                try {
                    sendEmail(email2!!)
                }catch (e:Exception){

                }

            }
            binding.contactUsMobile2.id -> {

                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    try {
                        callMobileNumber(mobile2!!)
                    }catch (e:Exception){

                    }
                }
            }
            binding.contactUsEmail3.id -> {
                try {
                    sendEmail(email3!!)
                }catch (e:Exception){

                }
            }
            binding.contactUsMobile3.id -> {
                if (ContextCompat.checkSelfPermission(
                        requireActivity(), Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE),
                        1
                    )
                } else {
                    try {
                        callMobileNumber(mobile3!!)
                    }catch (e:Exception){

                    }

                }
            }
        }
    }

    private fun selectedViews() {
        binding.contactUsEmail1.setOnClickListener(this)
        binding.contactUsEmail2.setOnClickListener(this)
        binding.contactUsEmail3.setOnClickListener(this)
        binding.contactUsMobile1.setOnClickListener(this)
        binding.contactUsMobile2.setOnClickListener(this)
        binding.contactUsMobile3.setOnClickListener(this)
    }

    private fun callMobileNumber(mobile: String) {
        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$mobile")
            requireActivity().startActivity(callIntent)
        } catch (e: Exception) {

        }

    }


    private fun sendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data =
                Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_SUBJECT, " موسسه انتظار")
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }
        } catch (e: Exception) {

        }

    }

    private fun getDetails() {
        viewModel.getCompanyDetails().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    try {
                        binding.progressBar.hideProgressBar()
                        binding.txtAddress.text = it.data!!.address
                        mobile1 = it.data.boss_phone
                        mobile2 = it.data.assistant_phone
                        mobile3 = it.data.helper_phone
                        email1 = it.data.boss_email
                        email2 = it.data.assistant_email
                        email3 = it.data.helper_email
                        binding.txtAdmin1.text = it.data.boss_name
                        binding.txtAdmin2.text = it.data.assistant_name
                        binding.txtAdmin3.text = it.data.helper_name
                    } catch (e: Exception) {

                    }

                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    Toast.makeText(requireContext(), "خطا در برقراری با سرور", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }


}