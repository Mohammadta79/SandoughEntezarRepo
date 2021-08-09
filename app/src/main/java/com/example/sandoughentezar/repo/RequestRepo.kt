package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.RequestModel
import com.example.sandoughentezar.models.RequestReplyModel
import com.example.sandoughentezar.models.StringResponseModel
import retrofit2.Response
import javax.inject.Inject

class RequestRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getRequests(params: HashMap<String, String>): Response<ArrayList<RequestModel>> =
        apiServiceResult.getRequests(params)

    suspend fun newRequest(params: HashMap<String, String>): Response<StringResponseModel> =
        apiServiceResult.newRequest(params)

    suspend fun requestReply(params: HashMap<String, String>): Response<RequestReplyModel> =
        apiServiceResult.requestReply(params)

}