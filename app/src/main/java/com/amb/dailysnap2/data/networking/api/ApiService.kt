package com.amb.dailysnap2.data.networking.api

import com.amb.dailysnap2.data.networking.response.BaseResponse
import com.amb.dailysnap2.data.networking.response.LoginResponse
import com.amb.dailysnap2.data.networking.response.StoryResponse
import com.amb.dailysnap2.data.request.LoginRequest
import com.amb.dailysnap2.data.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse


    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ) : StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
    ): BaseResponse
}