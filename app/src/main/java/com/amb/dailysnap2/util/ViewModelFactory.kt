package com.amb.dailysnap2.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amb.dailysnap2.data.repository.StoryRepository
import com.amb.dailysnap2.di.StoryInjection
import com.amb.dailysnap2.ui.maps.MapsViewModel
import com.amb.dailysnap2.ui.story.AddStoryViewModel
import com.amb.dailysnap2.ui.user.UserViewModel
import com.amb.dailysnap2.ui.story.MainViewModel

class ViewModelFactory (private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(StoryInjection.provideRepository(context))
            }.also { instance = it }
        }
    }
}