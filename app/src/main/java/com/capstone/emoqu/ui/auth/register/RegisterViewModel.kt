package com.capstone.emoqu.ui.auth.register

import androidx.lifecycle.ViewModel
import com.capstone.emoqu.data.remote.EmoQuRepository

class RegisterViewModel (private val emoQuRepository: EmoQuRepository) : ViewModel() {
    fun register(firstName: String, lastName: String, email: String, password: String)=emoQuRepository.register(firstName, lastName, email, password)
}