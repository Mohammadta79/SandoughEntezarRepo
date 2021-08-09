package com.example.sandoughentezar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.RequestListTemplateBinding
import com.example.sandoughentezar.interfaces.OnRequestItemClickListener
import com.example.sandoughentezar.models.RequestModel

class RequestAdapter(
    var context: Context,
    var list: ArrayList<RequestModel>,
    var listener: OnRequestItemClickListener
) : RecyclerView.Adapter<RequestAdapter.RequestVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestVH = RequestVH(
        LayoutInflater.from(context).inflate(R.layout.request_list_template, parent, false)
    )

    override fun onBindViewHolder(holder: RequestVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size


    inner class RequestVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding = RequestListTemplateBinding.bind(itemView)
        fun bindData(data: RequestModel) {
            var amount = "%,d".format(data.amount.toLong())
            binding.txtRequestDetails.text = data.details
            binding.txtRequestAmount.text = "$amount تومان "
            binding.txtRequestInstallment.text = data.num_of_installments + " قسط "
            binding.txtDate.text = data.date

            binding.rootRequest.setOnClickListener {
                listener.onRequestClick(data)
            }

        }

    }
}