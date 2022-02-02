package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class AuthRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun login(map: HashMap<String, String>): Flow<LoginResponseModel> =
        apiServiceResult.login(map)

    suspend fun register(
        requestBody: RequestBody
    ): Flow<RegisterResponseModel> =
        apiServiceResult.register(requestBody)

    suspend fun uploadImages(

        requestBody: RequestBody
    ): Flow<StringResponseModel> =
        apiServiceResult.uploadImages(requestBody)

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        apiServiceResult.validatePhone(params)

    suspend fun forgotPass(params: HashMap<String, String>): Flow<ForgotPassModel> =
        apiServiceResult.forgotPass(params)
}