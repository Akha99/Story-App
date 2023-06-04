package com.amb.dailysnap2.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null,
    ) = repository.addStory(token, file, description, lat, lon)

    fun getUser(): LiveData<User> = repository.getUserData()
}