package com.example.sandoughentezar.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTablayout()
        setupViewPager()
        tabSelected()
    }
    @Suppress("DEPRECATION")
    private class LoginPagerAdapter(fm: FragmentManager, var totalTabs: Int) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> LoginFragment()
                1 -> RegisterFragment()
                else -> null!!
            }
        }

        override fun getCount(): Int = totalTabs

    }

    private fun setupTablayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ورود"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ثبت نام"))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = LoginPagerAdapter(requireActivity().supportFragmentManager, 2)
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
    }

    private fun tabSelected() {
        @Suppress("DEPRECATION")
        binding.tabLayout.setOnTabSelectedListener(
            object : TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager) {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    super.onTabSelected(tab)
                    binding.viewPager.currentItem = tab.position
                }
            })
    }

}