package com.capstone.emoqu.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.emoqu.data.local.MoodModel
import com.capstone.emoqu.databinding.CarouselItemBinding

class MoodAdapter(
    private val moodList: List<MoodModel>,
    private val onItemClick: (MoodModel) -> Unit
): RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    var selectedPosition: Int = RecyclerView.NO_POSITION
    inner class MoodViewHolder(val binding: CarouselItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick.invoke(moodList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodViewHolder(binding)
    }

    override fun getItemCount(): Int = moodList.size

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moodList[position]
        holder.binding.apply {
            Glide.with(ivMoodImage)
                .load(mood.image)
                .into(ivMoodImage)
            tvMoodName.text = mood.name
            root.isSelected = selectedPosition == position
        }
    }
}