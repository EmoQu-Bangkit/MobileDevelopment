package com.capstone.emoqu.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.di.Injection
import com.capstone.emoqu.ui.add.AddActivityViewModel
import com.capstone.emoqu.ui.auth.login.LoginViewModel
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.ui.auth.pref.AuthViewModel
import com.capstone.emoqu.ui.auth.pref.dataStore
import com.capstone.emoqu.ui.auth.register.RegisterViewModel
import com.capstone.emoqu.ui.today.TodayViewModel

class ViewModelFactory(
    private val emoQuRepository: EmoQuRepository,
    private val authentication: AuthPreferences)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(emoQuRepository) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(emoQuRepository) as T
        }
        else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authentication) as T
        }
        else if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            return TodayViewModel(emoQuRepository, authentication) as T
        }
        else if (modelClass.isAssignableFrom(AddActivityViewModel::class.java)) {
            return AddActivityViewModel(emoQuRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    AuthPreferences.getInstance(context.dataStore)
                )
            }.also { instance = it }
        }
    }
}