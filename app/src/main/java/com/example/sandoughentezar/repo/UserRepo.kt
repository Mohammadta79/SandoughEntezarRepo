package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class UserRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getUserInfo(params: HashMap<String, String>): Response<UserModel> =
        apiServiceResult.getUserInfo(params)


    suspend fun updateProfile(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.updateProfile(params)
}