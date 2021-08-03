package com.example.sandoughentezar.ui.nav_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.MessageAdapter
import com.example.sandoughentezar.adapters.NewsAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentNewsBinding
import com.example.sandoughentezar.viewModels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val newsViewModel by viewModels<NewsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        getNews()
    }

    private fun getNews() {
        newsViewModel.getNews().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.size == 0) {
                        binding.txtNoNews.visibility = View.VISIBLE
                    } else {
                        binding.txtNoNews.visibility = View.INVISIBLE
                        binding.newsRV.apply {
                            adapter =
                                NewsAdapter(requireContext(), it.data)
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        }
                    }

                }
                Status.Failure -> {
                    binding.progressBar.hideProgressBar()
                    Toast.makeText(requireContext(),"خطا در برقراری ارتباط با سرور",Toast.LENGTH_SHORT).show()
                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
            }

        }
    }

}