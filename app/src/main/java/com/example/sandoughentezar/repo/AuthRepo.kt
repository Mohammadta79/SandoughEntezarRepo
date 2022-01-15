package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.ForgotPassModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class AuthRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun login(map: HashMap<String, String>): Response<LoginResponseModel> =
        apiServiceResult.login(map)

    suspend fun register(
        requestBody: RequestBody
    ): Response<StringResponseModel> =
        apiServiceResult.register(requestBody)

    suspend fun uploadImages(

        requestBody: RequestBody
    ): Response<StringResponseModel> =
        apiServiceResult.uploadImages( requestBody)

    suspend fun validatePhone(params: HashMap<String, String>): Response<ValidatePhoneResponseModel> =
        apiServiceResult.validatePhone(params)

    suspend fun forgotPass(params: HashMap<String, String>): Response<ForgotPassModel> =
        apiServiceResult.forgotPass(params)
}