package com.capstone.emoqu.ui.report

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.capstone.emoqu.R
import com.capstone.emoqu.component.CustomButtonTransparant
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.helper.DailyPredictionHelper
import com.capstone.emoqu.ui.MainActivity

class ReportSlideFragment : Fragment() {

    private lateinit var dailyPredictionHelper: DailyPredictionHelper
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var predictionContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report_slide, container, false)

        // Use the existing ViewModelFactory to initialize the ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        reportViewModel = ViewModelProvider(this, factory).get(ReportViewModel::class.java)

        dailyPredictionHelper = DailyPredictionHelper(requireContext(), reportViewModel) { predictionLabel ->
            // Handle the prediction result and update the UI
            updateUI(predictionLabel)
        }

        predictionContainer = view.findViewById(R.id.prediction_container)

        // Start observing the data
        dailyPredictionHelper.startObserving()

        val btnReport = view.findViewById<CustomButtonTransparant>(R.id.btn_report)
        btnReport.setOnClickListener {
            val targetFragment = ReportDetailFragment() // Replace with your target fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, targetFragment) // Make sure to replace R.id.fragment_container with your actual container ID
                .addToBackStack(null) // Optional: Add to back stack
                .commit()
        }

        return view
    }

    private fun updateUI(predictionLabel: String) {
        val layoutRes = when (predictionLabel) {
            "Active Explorer" -> R.layout.activity_active_explorer
            "Balanced Achiever" -> R.layout.activity_balanced_achiever
            "Fitness Fanatic" -> R.layout.activity_fitness_fanatic
            "Stressful Overload" -> R.layout.activity_stressful_overload
            else -> R.layout.activity_unknown // default or error layout
        }

        val layoutInflater = LayoutInflater.from(context)
        predictionContainer.removeAllViews()
        val customView = layoutInflater.inflate(layoutRes, predictionContainer, false)
        predictionContainer.addView(customView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dailyPredictionHelper.stopObserving()
    }
}
