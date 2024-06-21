package com.capstone.emoqu.ui.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.MoodModel
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.databinding.FragmentAddBinding
import com.capstone.emoqu.ui.MainActivity
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.utils.DateHelper
import com.capstone.emoqu.data.remote.Result
import android.app.AlertDialog

@Suppress("NAME_SHADOWING", "DEPRECATION")
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val addActivityViewModel: AddActivityViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: MoodAdapter
    private var selectedMood: MoodModel? = null
    private var selectedMoodPosition: Int = 2

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
        moodList.add(MoodModel(R.drawable.emoji_terrible, "Terrible", 1))
        moodList.add(MoodModel(R.drawable.emoji_bad, "Bad", 2))
        moodList.add(MoodModel(R.drawable.emoji_okay, "Okay", 3))
        moodList.add(MoodModel(R.drawable.emoji_good, "Good", 4))
        moodList.add(MoodModel(R.drawable.emoji_excellent, "Excellent", 5))

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

        if (selectedMoodPosition != RecyclerView.NO_POSITION) {
            binding.carouselMood.post {
                binding.carouselMood.scrollToPosition(selectedMoodPosition)
            }
        }

        setupAutoCompleteActivity()
        setupAutoCompleteDuration()

        binding.btnSave.setOnClickListener {
            Toast.makeText(context, "The Save button has been clicked", Toast.LENGTH_SHORT).show()
            insertActivity()
        }

        binding.btnBack.setOnClickListener {
            showConfirmationDialog()
        }

    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to cancel adding activity data?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Handle back press in this fragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Return to the previous fragment
                parentFragmentManager.popBackStack()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomMenu()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomMenu()
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
        val duration = binding.autoCompleteDuration.text.toString()
        val notes = binding.editTextView.text.toString()
        val currentDate = DateHelper.getCurrentDate()

        if (selectedMood != null && activityName.isNotBlank() && duration.isNotBlank()) {
            val duration = duration.toInt()
            val activity = ActivityEntity(
                date = currentDate,
                quality = selectedMood!!.quality,
                activity = activityName,
                duration = duration,
                notes = notes
            )

            addActivityViewModel.saveActivity(activity)
            addActivityViewModel.saveActivityResult.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        // Handle loading state if necessary
                    }

                    is Result.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Activity saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        requireContext().startActivity(intent)

                    }

                    is Result.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Failed to save activity: ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Please choose a mood, by tapping one of the emojis", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Don't forget to also fill in the activity and duration columns", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}