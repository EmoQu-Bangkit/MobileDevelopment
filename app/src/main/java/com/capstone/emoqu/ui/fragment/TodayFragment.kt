package com.capstone.emoqu.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.emoqu.R
import com.capstone.emoqu.databinding.ActivityRegisterBinding
import com.capstone.emoqu.databinding.FragmentTodayBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.ui.auth.login.LoginActivity
import com.capstone.emoqu.ui.auth.register.RegisterViewModel

class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val viewModel: TodayViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner){
            if(it.isEmpty()){
               navigateFromFragmentToActivity(LoginActivity::class.java)
            }
        }
    }

    private fun navigateFromFragmentToActivity(destinationClass: Class<*>) {
        val intent = Intent(requireActivity(), destinationClass)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}