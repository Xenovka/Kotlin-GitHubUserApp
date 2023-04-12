package com.dicoding.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.data.SettingPreferences

class ViewModelFactory private constructor(private val pref: SettingPreferences):
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(pref: SettingPreferences): ViewModelFactory {
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE = ViewModelFactory(pref)
            }

            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }

        throw java.lang.IllegalArgumentException("Unknown viewModel class")
    }
}