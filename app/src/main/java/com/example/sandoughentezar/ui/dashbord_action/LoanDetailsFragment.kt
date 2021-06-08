package com.example.sandoughentezar.ui.dashbord_action

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.FragmentLoanDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoanDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLoanDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }



}