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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.FragmentContactUsBinding


class ContactUsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentContactUsBinding
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
        selectedViews()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.contactUsEmail.id -> {
                sendEmail()
            }
            binding.contactUsMobile.id -> {
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
                    callMobileNumber()
                }
            }
            binding.contactUsPhone.id -> {
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
                    callPhoneNumber()
                }
            }
        }
    }

    private fun selectedViews() {
        binding.contactUsEmail.setOnClickListener(this)
        binding.contactUsMobile.setOnClickListener(this)
        binding.contactUsPhone.setOnClickListener(this)
    }

    private fun callMobileNumber() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + "+989164408396")
        requireActivity().startActivity(callIntent)
    }

    private fun callPhoneNumber() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + "+987152841275")
        requireActivity().startActivity(callIntent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data =
            Uri.parse("mailto:" + "mohammadta7879@gmail.com")
        intent.putExtra(Intent.EXTRA_SUBJECT, "مدیریت صندوق انتظار")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }


}