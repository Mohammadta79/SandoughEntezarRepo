package com.example.sandoughentezar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.PaymentRecordListTemplateBinding
import com.example.sandoughentezar.models.PaymentModel

class PaymentRecordsAdapter(
    var context: Context,
    var list: ArrayList<PaymentModel>
) : RecyclerView.Adapter<PaymentRecordsAdapter.PaymentRecordVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentRecordVH =
        PaymentRecordVH(
            LayoutInflater.from(context)
                .inflate(R.layout.payment_record_list_template, parent, false)
        )


    override fun onBindViewHolder(holder: PaymentRecordVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class PaymentRecordVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = PaymentRecordListTemplateBinding.bind(itemView)

        fun bindData(data: PaymentModel) {
            binding.txtAmount.text = (data.amount + "تومان")
            binding.txtAuthority.text = data.authority
            binding.txtDate.text = data.date
        }
    }

}