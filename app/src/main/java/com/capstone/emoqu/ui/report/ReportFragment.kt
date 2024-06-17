package com.capstone.emoqu.ui.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        monthSpinner = view.findViewById(R.id.spinner_month)
        yearSpinner = view.findViewById(R.id.spinner_year)

        // Mengisi spinner dengan data bulan
        val months = resources.getStringArray(R.array.month_array)
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        // Mengisi spinner dengan data tahun
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (2023..currentYear).toList().map { it.toString() }
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter

        qualityPredictionHelper = QualityPredictionHelper(context = requireContext(), reportViewModel = viewModel)

        processServerData()

        return view
    }

    private fun processServerData() {
        // Panggil fungsi processServerData() dari qualityPredictionHelper
        qualityPredictionHelper.processServerData()
    }

}