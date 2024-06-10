package com.capstone.emoqu.data.retrofit

import com.capstone.emoqu.data.response.LoginResponse
import com.capstone.emoqu.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("email") email:String,
        @Field("password") password: String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email:String,
        @Field("password") password: String
    ): LoginResponse
}