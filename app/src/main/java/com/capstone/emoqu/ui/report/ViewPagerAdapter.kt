package com.capstone.emoqu.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.emoqu.databinding.ItemSlideReportBinding

class ViewPagerAdapter(
    private var title:List<String>,
    private var types: List<String>,
    private var images: List<Int>,
    private var analysis: List<String>
): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(private val binding: ItemSlideReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, types: String, images: Int, analysis: String,position: Int) {
            binding.tvTitle.text = title
            binding.tvTypeReport.text = types
            binding.tvAnalysis.text = analysis
            if (position == 0) {
                Glide.with(binding.ivTypeReport.context)
                    .load(images)
                    .into(binding.ivTypeReport)
            } else if (position == 1) {
                Glide.with(binding.ivTypeReport.context)
                    .asGif()
                    .load(images)
                    .into(binding.ivTypeReport)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.Pager2ViewHolder {
        val binding = ItemSlideReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Pager2ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.bind(title[position], types[position], images[position], analysis[position], position)
    }

    override fun getItemCount(): Int {
        return title.size
    }

}