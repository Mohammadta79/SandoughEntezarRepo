package com.example.sandoughentezar.ui.message_action

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.MessageAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentMessageDetailsBinding
import com.example.sandoughentezar.interfaces.OnMessageClickListener
import com.example.sandoughentezar.models.MessageModel
import com.example.sandoughentezar.viewModels.MessageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMessageDetailsBinding
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    private lateinit var message_id: String
    private lateinit var title: String
    private lateinit var message: String
    private lateinit var date: String
    private val messageViewModel by viewModels<MessageViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message_id = it.get("message_id").toString()
            title = it.get("title").toString()
            message = it.get("message").toString()
            date = it.get("date").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun getReplyParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["message_id"] = message_id
        return params
    }


    private fun initViews() {

        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null).let {
            user_id = it.toString()
        }
        setMessageData()
        getReply()
    }


    private fun getReply() {
        messageViewModel.getReply(getReplyParams())
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        binding.progressBar.hideProgressBar()
                        if (it.data!!.message.isNotEmpty()) {
                            binding.rootReply.visibility = View.VISIBLE
                            binding.txtNoReply.visibility = View.GONE
                            binding.txtDateReply.text = it.data.date
                            binding.txtTitleReply.text = it.data.title
                            binding.txtMessageReply.text = it.data.message

                        }
                    }
                    Status.Failure -> {
                        binding.progressBar.hideProgressBar()

                    }
                    Status.Loading -> {
                       binding.progressBar.showProgressBar()
                    }
                }

            }
    }

    private fun setMessageData() {
        binding.txtDate.text = date
        binding.txtMessage.text = message
        binding.txtTitle.text = title
    }


}