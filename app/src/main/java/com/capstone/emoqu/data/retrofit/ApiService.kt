package com.capstone.emoqu.data.retrofit

import com.capstone.emoqu.data.response.AddActivityResponse
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.data.response.LoginResponse
import com.capstone.emoqu.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("email") email:String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/activity")
    suspend fun addactivity(
        @Field("quality") quality:Int,
        @Field("activities") activities:String,
        @Field("duration") duration:Int,
        @Field("notes") notes:String
    ): AddActivityResponse

    @GET("api/activities")
    suspend fun listactivity(
        @Query("quality") quality: Int,
        @Query("activities") activities: String,
        @Query("notes") notes: String
    ): ListActivityResponse
}