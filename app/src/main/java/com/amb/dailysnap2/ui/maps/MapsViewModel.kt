package com.amb.dailysnap2.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.data.repository.StoryRepository

class MapsViewModel (private val repository: StoryRepository): ViewModel() {

    fun getStoryLocation(token: String) =
        repository.getStoryLocation(token)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}