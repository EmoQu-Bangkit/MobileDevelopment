package com.capstone.emoqu.ui.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.emoqu.R
import com.capstone.emoqu.component.CustomButtonTransparant
import com.capstone.emoqu.helper.QualityPredictionHelper
import com.capstone.emoqu.ui.MainActivity
import com.capstone.emoqu.ui.ViewModelFactory

class ReportQualityFragment : Fragment() {

    private lateinit var qualityPredictionHelper: QualityPredictionHelper
    private lateinit var reportViewModel: ReportViewModel
    private lateinit var predictionContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_quality, container, false)

        // Use the existing ViewModelFactory to initialize the ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        reportViewModel = ViewModelProvider(this, factory).get(ReportViewModel::class.java)

        qualityPredictionHelper = QualityPredictionHelper(context = requireContext(), reportViewModel = reportViewModel)

        predictionContainer = view.findViewById(R.id.prediction_container)

        // Start observing the data
        qualityPredictionHelper.processServerData()

        // Observe the prediction result and update the UI
        reportViewModel.getPredictionResult().observe(viewLifecycleOwner) { predictionLabel ->
            updateUI(predictionLabel)
        }

        val btnReport = view.findViewById<CustomButtonTransparant>(R.id.btn_report)
        btnReport.setOnClickListener {
            val targetFragment = ReportSlideFragment() // Replace with your target fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, targetFragment) // Make sure to replace R.id.fragment_container with your actual container ID
                .addToBackStack(null) // Optional: Add to back stack
                .commit()
        }


        return view
    }

    private fun updateUI(predictionLabel: String) {
        val layoutRes = when (predictionLabel) {
            "Terrible" -> R.layout.layout_terrible
            "Bad" -> R.layout.layout_bad
            "Okay" -> R.layout.layout_okay
            "Good" -> R.layout.layout_good
            "Excellent" -> R.layout.layout_excellent
            else -> R.layout.layout_unknown // default or error layout
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
        qualityPredictionHelper.close()
    }
}
