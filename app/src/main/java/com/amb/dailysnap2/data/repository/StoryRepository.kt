package com.amb.dailysnap2.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.amb.dailysnap2.data.StoryPagingSource
import com.amb.dailysnap2.data.Result
import com.amb.dailysnap2.data.model.Story
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.data.networking.api.ApiService
import com.amb.dailysnap2.data.networking.response.BaseResponse
import com.amb.dailysnap2.data.networking.response.LoginResponse
import com.amb.dailysnap2.data.networking.response.StoryResponse
import com.amb.dailysnap2.data.preference.UserPreferences
import com.amb.dailysnap2.data.request.LoginRequest
import com.amb.dailysnap2.data.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception


class StoryRepository (private val pref: UserPreferences, private val apiService: ApiService) {

    fun getStory(token: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }

    fun userLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(LoginRequest(email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }


    fun userRegister(name: String, email: String, password: String): LiveData<Result<BaseResponse>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.register(RegisterRequest(name, email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null,
    ): LiveData<Result<BaseResponse>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.addStory(token, file, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            val errorMsg = e.message.toString()
            Log.d("Signup", errorMsg)
            emit(Result.Error(errorMsg))
        }
    }


    fun getStoryLocation(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryLocation(token, 1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserData(): LiveData<User> {
        return pref.getUserData().asLiveData()
    }

    suspend fun saveUserData(user: User) {
        pref.saveUserData(user)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}