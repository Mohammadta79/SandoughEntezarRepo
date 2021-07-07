package com.example.sandoughentezar.adapters

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.MessageListTemplateBinding
import com.example.sandoughentezar.interfaces.OnMessageClickListener
import com.example.sandoughentezar.models.MessageModel


class MessageAdapter(
    var context: Context,
    var list: ArrayList<MessageModel>,
    var listener: OnMessageClickListener
) : RecyclerView.Adapter<MessageAdapter.MessageVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageVH = MessageVH(
        LayoutInflater.from(context).inflate(R.layout.message_list_template, parent, false)
    )

    override fun onBindViewHolder(holder: MessageVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size


    inner class MessageVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding = MessageListTemplateBinding.bind(itemView)

        fun bindData(data: MessageModel) {
            binding.txtDate.text = data.date
            binding.txtTitle.text = data.title
            binding.txtMessage.text = data.message
            binding.txtMessage.movementMethod = ScrollingMovementMethod()
            binding.rootMessage.setOnClickListener { listener.onMessageClick(data) }
        }

    }


}