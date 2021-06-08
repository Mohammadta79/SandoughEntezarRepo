package com.example.sandoughentezar.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.databinding.DeffearedInstallmentListTemplateBinding
import com.example.sandoughentezar.interfaces.OnInstallmentClickListener
import com.example.sandoughentezar.models.InstallmentModel

class DeafreadInstallmentAdapter(
    var context: Context,
    var list: ArrayList<InstallmentModel>,
    var listener: OnInstallmentClickListener
) :
    RecyclerView.Adapter<DeafreadInstallmentAdapter.DffearedInstallmentVH>() {


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
            binding.txtAmount.text = data.amount
            binding.txtRow.text = data.id
            binding.txtStatus.text = data.status
            binding.txtDate.text = data.date
            when (data.status) {
                "پرداخت شده" -> {
                    binding.txtStatus.setTextColor(Color.GREEN)
                }
                "پرداخت نشده" -> {
                    binding.txtStatus.setTextColor(Color.RED)
                }
            }
            binding.btnPay.setOnClickListener { listener.onDeffearedInstallmentClick(data) }
            binding.linearRoot.setOnClickListener {

                if (data.status == "پرداخت نشده") {
                    binding.linearRoot.animate().translationX(300f).setDuration(1000).start()
                }

            }
        }


    }
}