package com.example.sandoughentezar.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.LoanRecordListTemplateBinding
import com.example.sandoughentezar.interfaces.OnLoanItemClickListener
import com.example.sandoughentezar.models.LoanModel

class LoanRecordsAdapter(
    var context: Context,
    var list: ArrayList<LoanModel>,
    var listener: OnLoanItemClickListener
) :
    RecyclerView.Adapter<LoanRecordsAdapter.LoanRecordsVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanRecordsVH =
        LoanRecordsVH(
            LayoutInflater.from(context).inflate(R.layout.loan_record_list_template, parent, false)
        )

    override fun onBindViewHolder(holder: LoanRecordsVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class LoanRecordsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = LoanRecordListTemplateBinding.bind(itemView)

        fun bindData(data: LoanModel) {
            var amount = "%,d".format(data.amount.toLong())
            binding.txtAmount.text = "$amount تومان "
            binding.txtFirstDate.text = data.start_date
            binding.txtLastDate.text = data.end_date
            if (data.status == "0") {
                binding.txtStatus.text = "باز"
                binding.txtStatus.setTextColor(Color.GREEN)
            } else {
                binding.txtStatus.text = "بسته"
                binding.txtStatus.setTextColor(Color.RED)
            }


            itemView.setOnClickListener { listener.onLoanItemClick(data) }
        }
    }

}