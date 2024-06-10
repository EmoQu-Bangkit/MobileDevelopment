package com.capstone.emoqu.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.capstone.emoqu.R
import com.capstone.emoqu.databinding.FragmentActivitiesBinding

class ActivitiesFragment : Fragment() {

    private val itemActivities = listOf(
        "Dating",
        "Eating",
        "Entertainment",
        "Study",
        "Sleep",
        "Self care",
        "Traveling",
        "Work",
        "Workout"
    )

    private val itemDuration = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24")

    private lateinit var _binding: FragmentActivitiesBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterActivities = ArrayAdapter(requireContext(), R.layout.list_items, itemActivities)
        val adapterDuration = ArrayAdapter(requireContext(), R.layout.list_items, itemDuration)

        binding.autoCompleteActivity.setAdapter(adapterActivities)
        binding.autoCompleteDuration.setAdapter(adapterDuration)

        val itemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(requireContext(), "Item: $itemSelected", Toast.LENGTH_SHORT).show()
        }

        binding.autoCompleteActivity.onItemClickListener = itemClickListener
        binding.autoCompleteDuration.onItemClickListener = itemClickListener
    }
}


