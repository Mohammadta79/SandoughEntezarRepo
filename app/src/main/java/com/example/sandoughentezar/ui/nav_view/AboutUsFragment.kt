package com.example.sandoughentezar.ui.nav_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentAboutUsBinding
import com.example.sandoughentezar.viewModels.CompanyDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding
    private val viewModel by viewModels<CompanyDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAboutUs()
    }

    private fun getAboutUs() {
        viewModel.getAboutUs().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    binding.txtAboutUs.text = it.data!!.about_us
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