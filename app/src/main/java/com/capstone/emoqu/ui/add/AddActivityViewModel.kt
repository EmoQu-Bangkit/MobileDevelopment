package com.capstone.emoqu.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.AddActivityResponse
import kotlinx.coroutines.launch

class AddActivityViewModel(
    private val emoQuRepository: EmoQuRepository
) : ViewModel() {

//    fun insertActivity(activity: ActivityEntity) {
//        viewModelScope.launch {
//            emoQuRepository.insertActivity(activity)
//        }
//    }

    fun addActivity(quality: Int, activities: String, duration: Int, notes: String): LiveData<Result<AddActivityResponse>> {
        return emoQuRepository.addActivity(quality, activities, duration, notes)
    }
}
