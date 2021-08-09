package com.example.sandoughentezar.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.DeffearedInstallmentListTemplateBinding
import com.example.sandoughentezar.interfaces.OnInstallmentClickListener
import com.example.sandoughentezar.models.InstallmentModel

class InstallmentAdapter(
    var context: Context,
    var list: ArrayList<InstallmentModel>,
    var listener: OnInstallmentClickListener
) :
    RecyclerView.Adapter<InstallmentAdapter.DffearedInstallmentVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DffearedInstallmentVH =
        DffearedInstallmentVH(
            LayoutInflater.from(context).inflate(
                R.layout.deffeared_installment_list_template, parent, false
            )
        )


    override fun onBindViewHolder(holder: DffearedInstallmentVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size


    inner class DffearedInstallmentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding = DeffearedInstallmentListTemplateBinding.bind(itemView)

        fun bindData(data: InstallmentModel) {
            var amount = "%,d".format(data.amount.toLong())
            binding.txtAmount.text = "$amount تومان "
            binding.txtDate.text = data.date
            when (data.status) {
                "1" -> {
                    binding.txtStatus.text = "پرداخت شده"
                    binding.txtStatus.setTextColor(Color.GREEN)
                }
                "0" -> {
                    binding.txtStatus.text = "پرداخت نشده"
                    binding.txtStatus.setTextColor(Color.RED)
                }
            }
            binding.btnPay.setOnClickListener { listener.onInstallmentClick(data) }
            binding.linearRoot.setOnClickListener {

                if (data.status == "0") {
                    if (binding.linearRoot.translationX == 0f) {
                        binding.linearRoot.animate().translationX(500f).setDuration(1000).start()
                    } else {
                        binding.linearRoot.animate().translationX(0f).setDuration(1000).start()
                    }
                } else {
                    Toast.makeText(context, "قسط مورد نظر پرداخت شده است", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }


    }
}