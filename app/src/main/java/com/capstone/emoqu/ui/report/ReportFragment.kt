package com.capstone.emoqu.ui.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.capstone.emoqu.R
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityDao
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.ml.QualityMoodModel
import java.util.Calendar

class ReportFragment : Fragment() {

    private lateinit var monthSpinner: Spinner
    private lateinit var yearSpinner: Spinner

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

        return view
    }

//    private lateinit var qualityMoodModel: QualityMoodModel
//    private lateinit var activityDao: ActivityDao
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        qualityMoodModel = QualityMoodModel(this)
//        val database = ActivityRoomDatabase.getInstance(this)
//        activityDao = database.activityDao()
//
//        // Observing LiveData from Room database
//        activityDao.getAllActivity().observe(this, Observer { dataEntries ->
//            // Preprocess data
//            val preprocessedData = qualityMoodModel.preprocessData(dataEntries)
//
//            // Example: Convert preprocessed data to the format expected by the model
//            // This step will vary depending on your model's input format
//            val inputData = preprocessedData.map { /* conversion logic */ }.toFloatArray()
//
//            // Predict using the TFLite model
//            val predictions = qualityMoodModel.predict(inputData)
//            println("Predictions: ${predictions.joinToString()}")
//        })
//    }
}
