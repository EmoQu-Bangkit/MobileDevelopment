package com.capstone.emoqu.ui.today

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.databinding.FragmentTodayBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.ui.auth.login.LoginActivity

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodayViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var todayAdapter: TodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false).apply {
            viewModel = this@TodayFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getSession().observe(viewLifecycleOwner, Observer { session ->
            if (session.isEmpty()) {
                navigateFromFragmentToActivity(LoginActivity::class.java)
            }
        })

        viewModel.getAllActivityCloud().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.viewSwitcher.showNext()
                }
                is Result.Success -> {
                    binding.viewSwitcher.showPrevious()
//                    todayAdapter.submitList(result.data.reversed())
                }
                is Result.Error -> {
                    binding.viewSwitcher.showNext()
                    // Handle error case
                }
            }
        })

        viewModel.firstName.observe(viewLifecycleOwner, Observer { firstName ->
            viewModel.lastName.observe(viewLifecycleOwner, Observer { lastName ->
                binding.username.text = "$firstName $lastName"
            })
        })
    }

    private fun setupRecyclerView() {
        todayAdapter = TodayAdapter()
        binding.rvFollows.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todayAdapter
        }
    }

    private fun navigateFromFragmentToActivity(destinationClass: Class<*>) {
        val intent = Intent(requireActivity(), destinationClass)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
