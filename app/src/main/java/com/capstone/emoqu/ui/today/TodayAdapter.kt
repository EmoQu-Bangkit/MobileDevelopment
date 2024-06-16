package com.capstone.emoqu.ui.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.databinding.ItemTodayBinding

class TodayAdapter : ListAdapter<ActivityEntity, TodayAdapter.TodayViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        val binding = ItemTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodayViewHolder(private val binding: ItemTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: ActivityEntity) {
            binding.apply {
                iconMoodToday.setImageResource(getMoodIcon(activity.quality))
                moodToday.text = getMoodText(activity.quality)
                notesToday.text = activity.notes
            }
        }

        private fun getMoodIcon(quality: Int): Int {
            return when (quality) {
                0 -> R.drawable.emoji_terrible
                1 -> R.drawable.emoji_bad
                2 -> R.drawable.emoji_okay
                3 -> R.drawable.emoji_good
                4 -> R.drawable.emoji_excellent
                else -> R.drawable.emoji_okay
            }
        }

        private fun getMoodText(quality: Int): String {
            return when (quality) {
                0 -> "Terrible"
                1 -> "Bad"
                2 -> "Okay"
                3 -> "Good"
                4 -> "Excellent"
                else -> "Unknown"
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ActivityEntity>() {
            override fun areItemsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ActivityEntity, newItem: ActivityEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
