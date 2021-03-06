package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.models.LoanModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class LoanRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getLoanRecord(params: HashMap<String, String>): Flow<ArrayList<LoanModel>> =
        apiServiceResult.getLoanRecord(params)

    suspend fun getLoanInstallment(params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>> =
        apiServiceResult.getLoanInstallment(params)
}