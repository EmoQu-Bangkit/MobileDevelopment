package com.capstone.emoqu.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.data.remote.EmoQuRepository
import kotlinx.coroutines.launch
import com.capstone.emoqu.data.remote.Result
import kotlinx.coroutines.Dispatchers


class AddActivityViewModel(
    private val emoQuRepository: EmoQuRepository
) : ViewModel() {


    private val _saveActivityResult = MutableLiveData<Result<Unit>>()
    val saveActivityResult: LiveData<Result<Unit>>
        get() = _saveActivityResult

    fun saveActivity(activity: ActivityEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = emoQuRepository.saveActivity(activity)
            _saveActivityResult.postValue(result)
        }
    }

}