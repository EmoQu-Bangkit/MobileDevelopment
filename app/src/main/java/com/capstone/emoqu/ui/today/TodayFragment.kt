package com.capstone.emoqu.ui.today

import ActivityAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.capstone.emoqu.R
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.Activity
import com.capstone.emoqu.databinding.FragmentTodayBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.ui.auth.login.LoginActivity
import com.capstone.emoqu.ui.detail.DetailActivityFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodayFragment : Fragment() {

    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodayViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardView = requireActivity().findViewById<CardView>(R.id.nav_card)
        cardView.visibility = View.VISIBLE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab_add)
        fab.visibility = View.VISIBLE

        setupRecyclerView()

        viewModel.getSession().observe(viewLifecycleOwner){
            if(it.isEmpty()){
               navigateFromFragmentToActivity(LoginActivity::class.java)
            } else {
                getProfileUser()
                getActivityData()
            }
        }

        activityAdapter.setOnItemClickCallback(object : ActivityAdapter.OnItemClickCallback {
            override fun onItemClicked(activity: Activity) {
                navigateToDetailFragment(activity)
            }
        })
    }

    private fun setupRecyclerView() {
        activityAdapter = ActivityAdapter()
        binding.rvFollows.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = activityAdapter
        }
    }

    private fun getProfileUser() {
        viewModel.getProfileUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = result.data.profile
                    binding.apply {
                        tvUsername.text = profile.lastName
                        if (profile.about.isEmpty()) {
                            tvAbout.text = requireContext().getString(R.string.about_default)
                        } else {
                            tvAbout.text = profile.about
                        }
                        if (profile.photoUrl.isEmpty()) {
                            ivUser.setImageResource(R.drawable.user)
                        } else {
                            Glide.with(requireActivity())
                                .load(profile.photoUrl)
                                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(ivUser)
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getActivityData() {
        viewModel.getActivityData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Data displayed successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val activities = result.data.activities
                    activityAdapter.submitList(activities)
                    binding.viewSwitcher.showPrevious()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.viewSwitcher.showNext()
                }
            }
        }
    }

    private fun navigateFromFragmentToActivity(destinationClass: Class<*>) {
        val intent = Intent(requireActivity(), destinationClass)
        startActivity(intent)
    }

    private fun navigateToDetailFragment(data: Activity) {
        val detailFragment = DetailActivityFragment().apply {
            arguments = Bundle().apply {
                putString("timestamp", data.timeStamp)
                putInt("quality", data.quality)
                putString("activities", data.activities)
                putInt("duration", data.duration)
                putString("notes", data.notes)
            }
        }
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, detailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}