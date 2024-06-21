package com.capstone.emoqu.ui.report

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.capstone.emoqu.R
import com.capstone.emoqu.helper.QualityPredictionHelper
import com.capstone.emoqu.ui.ViewModelFactory
import java.util.Calendar

class ReportFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private val viewModel: ReportViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private lateinit var qualityPredictionHelper: QualityPredictionHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the QualityPredictionHelper
        qualityPredictionHelper = QualityPredictionHelper(
            context = requireContext(),
            reportViewModel = viewModel
        )

        // Example usage of the helper
        qualityPredictionHelper.processServerData()

        // Observer for predictionResult
        viewModel.getPredictionResult().observe(viewLifecycleOwner) { result ->
            Log.d("ReportFragment", "Prediction Result: $result")
        }

        // Fetch activity data when view is created
        viewModel.getActivityData()

        // Set click listener on static_layout
        val staticLayout: View = view.findViewById(R.id.static_layout)
        staticLayout.setOnClickListener {
            val qualityConditionFragment = ReportQualityFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, qualityConditionFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        qualityPredictionHelper.close()
    }
}
