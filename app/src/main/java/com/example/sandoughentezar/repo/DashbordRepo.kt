package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.models.ScoreResponseModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.TotalModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class DashbordRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getMyScore(params: HashMap<String, String>): Flow<ScoreResponseModel> =
        apiServiceResult.getMyScore(params)

    suspend fun getDeferredinstallments(params: HashMap<String, String>): Response<ArrayList<InstallmentModel>> =
        apiServiceResult.getDeferredinstallments(params)

    suspend fun installmentPay(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.installmentPay(params)

    suspend fun getTotalPayment(params: HashMap<String, String>): Flow<TotalModel> =
        apiServiceResult.getTotalPayment(params)

    suspend fun getLastLoan(params: HashMap<String, String>): Flow<TotalModel> =
        apiServiceResult.getLastLoan(params)
}