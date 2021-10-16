package com.example.sandoughentezar.ui.nav_view

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.adapters.MessageAdapter
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentMessageBinding
import com.example.sandoughentezar.interfaces.OnMessageClickListener
import com.example.sandoughentezar.models.MessageModel
import com.example.sandoughentezar.viewModels.MessageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment(), OnMessageClickListener, View.OnClickListener {
    private lateinit var binding: FragmentMessageBinding
    private var sharedPref: SharedPreferences? = null
    private lateinit var user_id: String
    private val messageViewModel by viewModels<MessageViewModel>()
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        selectedViews()
    }

    private fun getUserParams(): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["member_id"] = user_id
        return params
    }

    private fun getNewMessageParams(title: String, message: String): HashMap<String, String> {
        var params = HashMap<String, String>()
        params["member_id"] = user_id
        params["title"] = title
        params["message"] = message
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
        messageViewModel.getMessage(getUserParams()).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Success -> {
                    binding.progressBar.hideProgressBar()
                    if (it.data!!.size == 0) {
                        binding.txtNoMessage.visibility = View.VISIBLE
                    } else {
                        binding.txtNoMessage.visibility = View.INVISIBLE
                        binding.messagesRV.apply {
                            adapter =
                                MessageAdapter(requireContext(), it.data, this@MessageFragment)
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
                    Toast.makeText(
                        requireContext(),
                        "خطا در برقراری ارتباط با سرور",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Status.Loading -> {
                    binding.progressBar.showProgressBar()
                }
            }

        }
    }

    override fun onMessageClick(data: MessageModel) {
        var bundle = Bundle()
        bundle.putString("message_id", data.id)
        bundle.putString("message", data.message)
        bundle.putString("title", data.title)
        bundle.putString("date", data.date)
        findNavController().navigate(R.id.action_messageFragment_to_messageDetailsFragment, bundle)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnNewMessage.id -> {
                setupNewMessageDialog()
            }
        }
    }

    private fun selectedViews() {
        binding.btnNewMessage.setOnClickListener(this)
    }

    private fun setupNewMessageDialog() {
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_new_message,
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

    private fun newMessage(title: String, message: String) {
        messageViewModel.newMessage(getNewMessageParams(title, message))
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.Success -> {
                        binding.progressBar.hideProgressBar()
                        if (it.data!!.message == "ok") {
                            Toast.makeText(
                                requireContext(),
                                "پیام با موفقیت ایجاد شد",
                                Toast.LENGTH_SHORT
                            ).show()
                            bottomSheetDialog!!.dismiss()
                            getMessages()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "خطا در ایجاد پیام",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Status.Failure -> {
                        binding.progressBar.hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            "خطا در برقراری ارتباط با سرور",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.Loading -> {
                        binding.progressBar.showProgressBar()
                    }
                }

            }
    }


}