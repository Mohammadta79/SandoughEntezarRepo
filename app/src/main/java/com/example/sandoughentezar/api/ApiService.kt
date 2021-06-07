package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import kotlinx.coroutines.flow.Flow
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(@FieldMap params: HashMap<String, String>): Flow<LoginResponseModel>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>

    @FormUrlEncoded
    @POST("validatePhone.php")
    suspend fun validatePhone(@FieldMap params: HashMap<String, String>):Flow<ValidatePhoneResponseModel>

}