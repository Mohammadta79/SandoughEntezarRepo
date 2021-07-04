package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.MessageModel
import com.example.sandoughentezar.models.StringResponseModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getMessage(params: HashMap<String, String>): Flow<ArrayList<MessageModel>> =
        apiServiceResult.getMessage(params)

    suspend fun newMessage(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.newMessage(params)

    suspend fun replyMessage(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.replyMessage(params)
}