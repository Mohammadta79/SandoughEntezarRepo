package com.example.sandoughentezar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.PaymentRecordListTemplateBinding
import com.example.sandoughentezar.models.PaymentModel
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat

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


           var pdformater = PersianDateFormat("Y/m/d")
            var persianDate = PersianDate()
            persianDate.shYear = data.date[0]
            persianDate.shMonth = data.date[1]
            persianDate.shDay = data.date[2]



            var amount = "%,d".format(data.amount.toLong())

            binding.txtAmount.text = "$amount تومان "
            binding.txtAuthority.text = data.authority.substring(30, 36)
            binding.txtDate.text = pdformater.format(persianDate)
        }
    }

}