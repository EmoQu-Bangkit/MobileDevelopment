package com.capstone.emoqu.ui.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.capstone.emoqu.R
import com.capstone.emoqu.component.CustomButton
import com.capstone.emoqu.data.local.MoodModel
import com.capstone.emoqu.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setting carousel
        val moodList = ArrayList<MoodModel>()
        moodList.add(MoodModel(R.drawable.emoji_terrible, "Terrible"))
        moodList.add(MoodModel(R.drawable.emoji_bad, "Bad"))
        moodList.add(MoodModel(R.drawable.emoji_okay, "Okay"))
        moodList.add(MoodModel(R.drawable.emoji_good, "Good"))
        moodList.add(MoodModel(R.drawable.emoji_excellent, "Excellent"))

        val adapter = MoodAdapter(moodList)

        binding.apply {
            carouselMood.adapter = adapter
            carouselMood.set3DItem(true)
            carouselMood.setAlpha(true)
            carouselMood.setInfinite(false)
        }

        //setting dropdown activity
        val activityArray = resources.getStringArray(R.array.activity_array)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityArray)
        binding.autoCompleteActivity.setAdapter(arrayAdapter)
        binding.autoCompleteActivity.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val itemSelected = adapterView.getItemAtPosition(i)
            binding.edActivity.hint = ""
            Toast.makeText(requireContext(), "Item: $itemSelected", Toast.LENGTH_SHORT).show()
        }

        //setting dropdown duration
        val minutes = (15..720 step 15).map { it.toString() }
        val durationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, minutes)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_duration)
        autoCompleteTextView.setAdapter(durationAdapter)
        binding.autoCompleteDuration.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val itemSelected = adapterView.getItemAtPosition(i)
            binding.edDuration.hint = ""
            Toast.makeText(requireContext(), "Selected Minute: $itemSelected", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}