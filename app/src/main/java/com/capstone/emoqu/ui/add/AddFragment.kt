package com.capstone.emoqu.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.MoodModel
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.databinding.FragmentAddBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.utils.DateHelper

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val addActivityViewModel: AddActivityViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: MoodAdapter
    private var selectedMood: MoodModel? = null

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
        moodList.add(MoodModel(R.drawable.emoji_terrible, "Terrible", 0))
        moodList.add(MoodModel(R.drawable.emoji_bad, "Bad", 1))
        moodList.add(MoodModel(R.drawable.emoji_okay, "Okay", 2))
        moodList.add(MoodModel(R.drawable.emoji_good, "Good", 3))
        moodList.add(MoodModel(R.drawable.emoji_excellent, "Excellent", 4))

        adapter = MoodAdapter(moodList) { selectedMood ->
            this.selectedMood = selectedMood
            Toast.makeText(requireContext(), "Selected Mood: ${selectedMood.name}", Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            carouselMood.adapter = adapter
            carouselMood.set3DItem(true)
            carouselMood.setAlpha(true)
            carouselMood.setInfinite(false)
        }

        setupAutoCompleteActivity()
        setupAutoCompleteDuration()

        //insert data to database
        binding.btnSave.setOnClickListener {
            insertActivity()
        }

    }

    private fun setupAutoCompleteActivity() {
        val activityArray = resources.getStringArray(R.array.activity_array)
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityArray)
        binding.autoCompleteActivity.setAdapter(arrayAdapter)
        binding.autoCompleteActivity.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val itemSelected = adapterView.getItemAtPosition(i)
            binding.edActivity.hint = ""
            Toast.makeText(requireContext(), "Item: $itemSelected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAutoCompleteDuration() {
        val minutes = (15..720 step 15).map { it.toString() }
        val durationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, minutes)
        val autoCompleteTextView = view?.findViewById<AutoCompleteTextView>(R.id.auto_complete_duration)
        autoCompleteTextView?.setAdapter(durationAdapter)
        binding.autoCompleteDuration.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val itemSelected = adapterView.getItemAtPosition(i)
            binding.edDuration.hint = ""
            Toast.makeText(requireContext(), "Selected Minute: $itemSelected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertActivity() {
        val activityName = binding.autoCompleteActivity.text.toString()
        val duration = binding.autoCompleteDuration.text.toString().toIntOrNull() ?: 0
        val notes = binding.editTextView.text.toString()
        val currentDate = DateHelper.getCurrentDate()

        if (selectedMood != null) {
            val activityEntity = ActivityEntity(
                date = currentDate,
                quality = selectedMood!!.quality,
                activity = activityName,
                duration = duration,
                notes = notes.ifBlank { null }
            )
            addActivityViewModel.insertActivity(activityEntity)
            Toast.makeText(context, "Activity added successfully", Toast.LENGTH_SHORT).show()
            //view?.findNavController()?.navigate(R.id.action_addFragment_to_todayFragment)
        } else {
            Toast.makeText(context, "Please choose a mood, by tapping one of the emojis", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}