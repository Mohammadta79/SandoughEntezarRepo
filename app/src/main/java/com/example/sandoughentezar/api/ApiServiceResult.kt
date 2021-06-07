package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiServiceResult @Inject constructor(var apiService: ApiService) {

    suspend fun login(params: HashMap<String, String>): Flow<LoginResponseModel> =
        apiService.login(params)

    suspend fun register(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.register(params)

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        apiService.validatePhone(params)

    suspend fun getMyScore(params: HashMap<String, String>): Flow<ScoreResponseModel> =
        apiService.getMyScore(params)

    suspend fun getDeferredinstallments(params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>> =
        apiService.getDeferredinstallments(params)
}