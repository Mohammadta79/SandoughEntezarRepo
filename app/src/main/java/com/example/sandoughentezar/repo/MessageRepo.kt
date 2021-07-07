package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.MessageModel
import com.example.sandoughentezar.models.ReplyModel
import com.example.sandoughentezar.models.StringResponseModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class MessageRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getMessage(params: HashMap<String, String>): Response<ArrayList<MessageModel>> =
        apiServiceResult.getMessage(params)

    suspend fun newMessage(params: HashMap<String, String>): Response<StringResponseModel> =
        apiServiceResult.newMessage(params)

    suspend fun getReply(params: HashMap<String, String>): Response<ReplyModel> =
        apiServiceResult.getReply(params)
}