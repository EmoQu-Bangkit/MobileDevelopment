import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.emoqu.R
import com.capstone.emoqu.data.response.Activity
import com.capstone.emoqu.databinding.ItemTodayBinding

class ActivityAdapter : ListAdapter<Activity, ActivityAdapter.ActivityViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val binding = ItemTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ActivityViewHolder(private val binding: ItemTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(activity: Activity) {
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(activity)
            }
            binding.apply {
                iconMoodToday.setImageResource(getMoodIcon(activity.quality))
                moodToday.text = getMoodText(activity.quality)
                activities.text = activity.activities
                notesToday.text = activity.notes
            }
        }

        private fun getMoodIcon(quality: Int): Int {
            return when (quality) {
                1 -> R.drawable.emoji_terrible
                2 -> R.drawable.emoji_bad
                3 -> R.drawable.emoji_okay
                4 -> R.drawable.emoji_good
                5 -> R.drawable.emoji_excellent
                else -> R.drawable.emoji_okay
            }
        }

        private fun getMoodText(quality: Int): String {
            return when (quality) {
                1 -> "Terrible"
                2 -> "Bad"
                3 -> "Okay"
                4 -> "Good"
                5 -> "Excellent"
                else -> "Unknown"
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(activity: Activity)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Activity>() {
            override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
