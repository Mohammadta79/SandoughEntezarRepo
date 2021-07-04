package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.NewsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getNews(): Flow<ArrayList<NewsModel>> = apiServiceResult.getNews()
}