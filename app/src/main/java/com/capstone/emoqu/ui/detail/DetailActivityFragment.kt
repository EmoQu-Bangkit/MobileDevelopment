package com.capstone.emoqu.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.capstone.emoqu.R
import com.capstone.emoqu.databinding.FragmentDetailActivityBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivityFragment : Fragment() {

    private var _binding: FragmentDetailActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardView = requireActivity().findViewById<CardView>(R.id.nav_card)
        cardView.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab_add)
        fab.visibility = View.GONE

        arguments?.let { args ->
            binding.apply {
                tvTimestamp.text = args.getString("timestamp")
                tvActivityText.text = args.getString("activities")
                tvDurationText.text = args.getInt("duration").toString()
                tvNotesText.text = args.getString("notes")
                val quality = args.getInt("quality")
                val qualityImageResource = getMoodIcon(quality)
                ivMoodImage.setImageResource(qualityImageResource)
            }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}