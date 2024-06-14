package com.capstone.emoqu.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.data.remote.EmoQuRepository
import kotlinx.coroutines.launch

class AddActivityViewModel (private val emoQuRepository: EmoQuRepository) : ViewModel() {

    fun insertActivity(activity: ActivityEntity) {
        viewModelScope.launch {
            emoQuRepository.insertActivity(activity)
        }
    }
}