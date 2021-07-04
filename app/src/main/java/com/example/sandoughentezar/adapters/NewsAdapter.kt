package com.example.sandoughentezar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sandoughentezar.R
import com.example.sandoughentezar.databinding.NewsListTemplateBinding
import com.example.sandoughentezar.models.NewsModel

class NewsAdapter(var context: Context, var list: ArrayList<NewsModel>) :
    RecyclerView.Adapter<NewsAdapter.NewsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH = NewsVH(
        LayoutInflater.from(context).inflate(R.layout.news_list_template, parent, false)
    )

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class NewsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = NewsListTemplateBinding.bind(itemView)
        fun bindData(data: NewsModel) {
            binding.txtTitle.text = data.title
            binding.txtDate.text = data.date
            binding.txtNews.text = data.news
        }
    }
}