package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.PaymentModel
import com.example.sandoughentezar.models.StringResponseModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class PaymentRepo @Inject constructor(var apiServiceResult: ApiServiceResult) {

    suspend fun getRecordPayment(params: HashMap<String, String>): Response<ArrayList<PaymentModel>> =
        apiServiceResult.getRecordPayment(params)

    suspend fun newPayment(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiServiceResult.newPayment(params)
}