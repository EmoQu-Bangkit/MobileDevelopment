package com.capstone.emoqu.ui.report

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.capstone.emoqu.R
import com.capstone.emoqu.databinding.FragmentReportDetailBinding
import com.capstone.emoqu.helper.DailyPredictionHelper
import com.capstone.emoqu.ui.ViewModelFactory
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.ui.MainActivity

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private val viewModel: ReportViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var dailyPredictionHelper: DailyPredictionHelper

    // Normal ranges for activities
    private val normalRanges = mapOf(
        "Dating" to (30 to 60),
        "Eating" to (90 to 120),
        "Entertainment" to (60 to 180),
        "Self Care" to (30 to 60),
        "Sleep" to (420 to 540),
        "Study" to (120 to 240),
        "Travelling" to (60 to 120),
        "Work" to (420 to 540),
        "Workout" to (30 to 60)
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Initialize PieChart
        pieChart = binding.root.findViewById(R.id.summary_duration_graph)

        // Initialize DailyPredictionHelper
//        dailyPredictionHelper = DailyPredictionHelper(requireContext(), viewModel)
//        dailyPredictionHelper.startObserving()

        viewModel.getPredictionResult().observe(viewLifecycleOwner) { result ->
            // Handle prediction result if needed
        }

        // Fetch activity data when view is created
        viewModel.getActivityData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    // Process preprocessing data dan generate output
                    val activities = result.data.activities
                    val formattedData = dailyPredictionHelper.formatData(activities)

                    // Process modelling dan generate output
                    val predictionResult = dailyPredictionHelper.performInference(formattedData)
                    val predictionInteger = dailyPredictionHelper.convertResultToInt(predictionResult)
                    val predictionLabel = dailyPredictionHelper.labelPredictionResult(predictionInteger)

                    // Visualize data
                    visualizeData(pieChart, formattedData)

                    // Generate and display recommendations
                    val recommendations = generateRecommendations(formattedData)
                    displayRecommendations(recommendations)
                }
                is Result.Error -> {
                    Log.e("ReportDetailFragment", "Failed to fetch activity data: ${result.error}")
                }
                else -> {
                    Log.e("ReportDetailFragment", "Unknown result type: $result")
                }
            }
        }
    }

    // Visualize data
    private fun visualizeData(pieChart: PieChart, data: List<Map<String, Any>>) {
        // Assume each map in the data list represents a day's activities
        val dayData = data.firstOrNull() ?: return // Use the first day's data for visualization
        val pieEntries = mutableListOf<PieEntry>()

        for ((activity, duration) in dayData) {
            if (activity != "Time_Stamp") {
                pieEntries.add(PieEntry((duration as Int).toFloat(), activity))
            }
        }

        // Define colors for the pie chart
        val colors = listOf(
            Color.rgb(255, 0, 0),
            Color.rgb(255, 128, 0),
            Color.rgb(252, 217, 0),
            Color.rgb(0, 255, 0),
            Color.rgb(0, 153, 0),
            Color.rgb(0, 255, 255),
            Color.rgb(0, 128, 255),
            Color.rgb(127, 0, 255),
            Color.rgb(255, 0, 255)
        )

        // Create a PieDataSet with the entries and configure its appearance
        val dataSet = PieDataSet(pieEntries, "").apply {
            this.colors = colors
            this.sliceSpace = 3f
            this.selectionShift = 5f
        }

        // Create PieData with the dataSet and configure its appearance
        val pieData = PieData(dataSet).apply {
            setValueTextSize(10f)
            setValueTextColor(Color.BLACK)
        }

        // Configure the PieChart
        pieChart.apply {
            this.data = pieData
            description.isEnabled = false
            isRotationEnabled = true
            setEntryLabelColor(Color.TRANSPARENT)
            setEntryLabelTextSize(12f)
            setExtraOffsets(50f, 3f, 5f, 5f)
            legend.apply {
                form = Legend.LegendForm.SQUARE
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                textSize = 12f
            }
            animateY(2000)
        }

        pieChart.invalidate() // Refresh chart
    }

    // Generate recommendations
    private fun generateRecommendations(data: List<Map<String, Any>>): List<String> {
        val recommendations = mutableListOf<String>()
        val dayData = data.firstOrNull() ?: return recommendations // Use the first day's data for recommendations

        for ((activity, duration) in dayData) {
            if (activity != "Time_Stamp") {
                val normalRange = normalRanges[activity]
                if (normalRange != null) {
                    val durationInt = duration as Int
                    when {
                        durationInt < normalRange.first -> recommendations.add("- Increase time spent on $activity.")
                        durationInt > normalRange.second -> recommendations.add("- Decrease time spent on $activity.")
                    }
                }
            }
        }

        return recommendations
    }

    // Display recommendations
    private fun displayRecommendations(recommendations: List<String>) {
        val recommendationsText = recommendations.joinToString(separator = "\n")
        binding.recommend.findViewById<TextView>(R.id.activity_recommend).text = recommendationsText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
