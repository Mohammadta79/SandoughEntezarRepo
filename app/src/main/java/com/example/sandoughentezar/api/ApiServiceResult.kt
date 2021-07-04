package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ApiServiceResult @Inject constructor(var apiService: ApiService) {

    suspend fun login(params: HashMap<String, String>): Response<LoginResponseModel> =
        apiService.login(params)

    suspend fun register(params: HashMap<String, String>): Response<StringResponseModel> =
        apiService.register(params)

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        apiService.validatePhone(params)

    suspend fun getMyScore(params: HashMap<String, String>): Flow<ScoreResponseModel> =
        apiService.getMyScore(params)

    suspend fun getDeferredinstallments(params: HashMap<String, String>): Response<ArrayList<InstallmentModel>> =
        apiService.getDeferredinstallments(params)

    suspend fun installmentPay(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.installmentPay(params)

    suspend fun getUserInfo(params: HashMap<String, String>): Response<UserModel> =
        apiService.getUSerInfo(params)

    suspend fun getRecordPayment(params: HashMap<String, String>): Flow<ArrayList<PaymentModel>> =
        apiService.getRecordPayment(params)

    suspend fun newPayment(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.newPayment(params)

    suspend fun getLoanRecord(params: HashMap<String, String>): Flow<ArrayList<LoanModel>> =
        apiService.getLoanRecord(params)

    suspend fun getLoanInstallment(params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>> =
        apiService.getLoanInstallment(params)

    suspend fun getTotalPayment(params: HashMap<String, String>): Flow<TotalModel> =
        apiService.getTotalPayment(params)

    suspend fun getLastLoan(params: HashMap<String, String>): Flow<TotalModel> =
        apiService.getLastLoan(params)

    suspend fun updateProfile(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.updateProfile(params)

    suspend fun getMessage(params: HashMap<String, String>): Flow<ArrayList<MessageModel>> =
        apiService.getMessage(params)

    suspend fun newMessage(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.newMessage(params)

    suspend fun replyMessage(params: HashMap<String, String>): Flow<StringResponseModel> =
        apiService.replyMessage(params)

    suspend fun getNews(): Flow<ArrayList<NewsModel>> = apiService.getNews()
}

