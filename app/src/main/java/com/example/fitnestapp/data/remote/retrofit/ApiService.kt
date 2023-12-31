package com.example.fitnestapp.data.remote.retrofit

import com.example.fitnestapp.data.local.EditUserRequest
import com.example.fitnestapp.data.remote.response.ProfileResponse
import com.example.fitnestapp.data.remote.response.ResponseDietPref
import com.example.fitnestapp.data.remote.response.ResponseFood
import com.example.fitnestapp.data.remote.response.ResponseGoal
import com.example.fitnestapp.data.remote.response.ResponseLevel
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.remote.response.ResponseTargetMuscle
import com.example.fitnestapp.data.remote.response.ResponseUser
import com.example.fitnestapp.data.remote.response.ResponseWorkout
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.Date

interface ApiService {
    @FormUrlEncoded
    @POST("api/v1/auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String
    ): ResponseRegist

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): ResponseLogin

    @FormUrlEncoded
    @POST("api/v1/user/profile")
    suspend fun insertProfile(
        @Header("Authorization") token: String,
        @Field("firstname") firstname: String,
        @Field("lastname") lastname: String,
        @Field("gender") gender: String,
        @Field("dateOfBirth") dateOfBirth: Date,
        @Field("height") height: Int,
        @Field("weight") weight: Int,
        @Field("goalId") goalId: List<String>,
        @Field("levelId") levelId:String,
        @Field("targetMuscleId") targetMuscleId: List<String>,
        @Field("dietPrefId") dietPrefId:String
    ): ProfileResponse

    @GET("api/v1/user/profile/")
    suspend fun getUserProfile(
    ): ProfileResponse

    @GET("api/v1/goal/all")
    suspend fun getGoal() : Response<ResponseGoal>



    @GET("api/v1/level/all")
    suspend fun getLevel() : Response<ResponseLevel>

    @GET("api/v1/target-muscle/all")
    suspend fun getTargetMuscle() : Response<ResponseTargetMuscle>

    @GET("api/v1/diet-pref/all")
    suspend fun getDietPref() : Response<ResponseDietPref>

    @GET("api/v1/user")
    suspend fun getUser(): Response<ResponseUser>

    @PUT("api/v1/user")
    suspend fun updateUser(@Body request: EditUserRequest): Response<ResponseUser>

    @GET("/")
    suspend fun getFood(
    ): Response<ResponseFood>


    @GET("/")
    suspend fun getWorkout(
    ): Response<ResponseWorkout>

}