package com.amb.dailysnap2.data.networking.response

import com.amb.dailysnap2.data.model.Login
import com.google.gson.annotations.SerializedName
data class LoginResponse(
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String?,
    @SerializedName("loginResult")
    val loginResult: Login?,
)
