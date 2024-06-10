package com.capstone.emoqu.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.emoqu.R

class TodayFragment : Fragment() {

    private lateinit var viewSwitcher: ViewSwitcher
    private lateinit var recyclerView: RecyclerView
    private lateinit var noDataImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        viewSwitcher = view.findViewById(R.id.view_switcher)
        recyclerView = view.findViewById(R.id.rv_follows)
        noDataImageView = view.findViewById(R.id.iv_no_data)

        // Logika untuk menentukan apakah ada data atau tidak
        val data = getData()
        if (data.isEmpty()) {
            viewSwitcher.displayedChild = 1 // Menampilkan ImageView
        } else {
            viewSwitcher.displayedChild = 0 // Menampilkan RecyclerView
            setupRecyclerView(data)
        }

        return view
    }

    private fun getData(): List<Any> {
        return listOf()
    }

    private fun setupRecyclerView(data: List<Any>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = YourAdapter(data)
    }
}