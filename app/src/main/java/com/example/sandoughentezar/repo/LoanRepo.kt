package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.LoanModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoanRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {
    suspend fun getLoanRecord(params: HashMap<String, String>): Flow<ArrayList<LoanModel>> =
        apiServiceResult.getLoanRecord(params)
}