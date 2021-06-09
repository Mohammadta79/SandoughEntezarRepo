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
class MessageDetailsFragment : Fragment(), OnMessageClickListener, View.OnClickListener {

    private lateinit var binding: FragmentMessageDetailsBinding
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    private lateinit var message_id: String
    private val messageViewModel by viewModels<MessageViewModel>()
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message_id = it.get("message_id").toString()
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
        selectedViews()
    }

    private fun selectedViews() {
        binding.btnReply.setOnClickListener(this)
    }

    private fun setupNewMessageDialog() {
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_paymant,
            requireActivity().findViewById<LinearLayout>(R.id.root_dialog_new_message)
        )
        bottomSheetView!!.findViewById<Button>(R.id.btn_create_message).setOnClickListener {
            var title = bottomSheetView!!.findViewById<EditText>(R.id.txt_new_title).text.toString()
            var message =
                bottomSheetView!!.findViewById<EditText>(R.id.txt_new_message).text.toString()
            newMessage(title, message)
        }
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }

    private fun getMessageParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["message_id"] = user_id
        return params
    }

    private fun getReplyMessageParams(title: String, message: String): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["user_id"] = user_id
        params["title"] = title
        params["message"] = message
        params["message_id"] = message_id
        return params
    }

    private fun initViews() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        sharedPref = activity?.getSharedPreferences("shp", Context.MODE_PRIVATE)
        sharedPref!!.getString("user_id", null).let {
            user_id = it.toString()
        }
        getMessages()
    }

    private fun getMessages() {
        messageViewModel.getMessage(getMessageParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.messageRV.apply {
                        adapter =
                            MessageAdapter(requireContext(), it.data!!, this@MessageDetailsFragment)
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }


                }
                Status.Failure -> {

                }
                Status.Loading -> {
                    //TODO:Show progressbar
                }
            }

        }
    }

    private fun newMessage(title: String, message: String) {
        messageViewModel.replyMessage(getReplyMessageParams(title, message))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        if (it.data!!.status == "ok") {
                            Toast.makeText(
                                requireContext(),
                                "پاسخ با موفقیت ایجاد شد",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Status.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            "خطا در ایجاد پیام",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.Loading -> {
                        //TODO:Show progressbar
                    }
                }

            }
    }

    override fun onMessageClick(data: MessageModel) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnReply.id -> {
                setupNewMessageDialog()
            }
        }
    }


}