package com.capstone.emoqu.data.retrofit

import com.capstone.emoqu.data.response.AddActivityResponse
import com.capstone.emoqu.data.response.AddReportResponse
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.data.response.ListReportResponse
import com.capstone.emoqu.data.response.LoginResponse
import com.capstone.emoqu.data.response.ProfileResponse
import com.capstone.emoqu.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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
        @Field("email") email:String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("api/activity")
    suspend fun addActivity(
        @Field("quality") quality:Int,
        @Field("activities") activity:String,
        @Field("duration") duration:Int,
        @Field("notes") notes:String
    ): AddActivityResponse

    @GET("api/activities")
    suspend fun listActivity(): ListActivityResponse

    @GET("api/profile")
    suspend fun getProfile(): ProfileResponse

    @FormUrlEncoded
    @POST("api/report")
    suspend fun addReport(
        @Field("timeStamp") timeStamp:String,
        @Field("dating") dating:Float,
        @Field("eating") eating:Float,
        @Field("entertainment") entertainment:Float,
        @Field("selfCare") selfCare:Float,
        @Field("sleep") sleep:Float,
        @Field("study") study:Float,
        @Field("traveling") traveling:Float,
        @Field("work") work:Float,
        @Field("workout") workout:Float,
        @Field("predictedDayCondition") predictedDayCondition:String,
        @Field("predictedDayLabel") predictedDayLabel:String,
        @Field("positif") positif:Int,
        @Field("negatif") negatif:Int,
        @Field("netral") netral:Int,
        @Field("dateTips") dateTips:String,
        @Field("eatTips") eatTips:String,
        @Field("entertainment") entertainmentTips:String,
        @Field("selfCareTips") selfCareTips:String,
        @Field("sleepTips") sleepTips:String,
        @Field("studyTips") studyTips:String,
        @Field("travelingTips") travelingTips:String,
        @Field("workTips") workTips:String,
        @Field("workoutTips") workoutTips:String
    ): AddReportResponse

    @GET("api/reports")
    suspend fun getListReport(): ListReportResponse
}