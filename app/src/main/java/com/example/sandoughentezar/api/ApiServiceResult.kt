package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiServiceResult @Inject constructor(var apiService: ApiService) {

    suspend fun login(params: HashMap<String, String>): Flow<LoginResponseModel> =
        apiService.login(params)

    suspend fun register(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.register(params)

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        apiService.validatePhone(params)
}