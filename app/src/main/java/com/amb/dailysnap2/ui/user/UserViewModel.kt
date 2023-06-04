package com.amb.dailysnap2.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.data.repository.StoryRepository
import kotlinx.coroutines.launch

class UserViewModel (private val repository: StoryRepository) : ViewModel() {
    fun userLogin(email: String, password: String) = repository.userLogin(email, password)

    fun userRegister(name: String, email: String, password: String) =
        repository.userRegister(name, email, password)

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUserData(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}