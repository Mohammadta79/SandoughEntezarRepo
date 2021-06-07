package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun login(params: HashMap<String, String>): Flow<LoginResponseModel> =
        apiServiceResult.login(params)

    suspend fun register(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.register(params)

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        apiServiceResult.validatePhone(params)
}