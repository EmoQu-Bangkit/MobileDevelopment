package com.capstone.emoqu.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.emoqu.data.local.MoodModel
import com.capstone.emoqu.databinding.CarouselItemBinding

class MoodAdapter(private val moodList: List<MoodModel>): RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(val binding: CarouselItemBinding): RecyclerView.ViewHolder(binding.root)

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
            ivMoodImage.setOnClickListener {
                Toast.makeText(it.context, mood.name, Toast.LENGTH_SHORT).show()
            }
        }
    }
}