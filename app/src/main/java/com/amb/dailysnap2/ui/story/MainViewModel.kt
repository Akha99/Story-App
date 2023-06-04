package com.amb.dailysnap2.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amb.dailysnap2.data.model.Story
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.data.repository.StoryRepository

class MainViewModel (private val repository: StoryRepository) : ViewModel() {

    fun getStory(token: String): LiveData<PagingData<Story>> {
        return  repository.getStory(token).cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}