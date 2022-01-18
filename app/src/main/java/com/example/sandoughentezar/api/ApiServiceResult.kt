package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ApiServiceResult @Inject constructor(var apiService: ApiService) {

    suspend fun login(map: HashMap<String, String>): Flow<LoginResponseModel> =
        flow { emit(apiService.login(map)) }

    suspend fun register(
        requestBody: RequestBody
    ): Flow<StringResponseModel> = flow { emit(apiService.register(requestBody)) }


    suspend fun getUserInfo(params: HashMap<String, String>): Flow<UserModel> =
        flow { emit(apiService.getUserInfo(params)) }

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        flow { emit(apiService.validatePhone(params)) }

    suspend fun uploadImages(
        requestBody: RequestBody
    ): Response<StringResponseModel> = apiService.uploadImages(requestBody)


    suspend fun getMyScore(params: HashMap<String, String>): Response<ScoreResponseModel> =
        apiService.getMyScore(params)

    suspend fun getDeferredinstallments(params: HashMap<String, String>): Response<ArrayList<InstallmentModel>> =
        apiService.getDeferredinstallments(params)


    suspend fun getRecordPayment(params: HashMap<String, String>): Response<ArrayList<PaymentModel>> =
        apiService.getRecordPayment(params)


    suspend fun getLoanRecord(params: HashMap<String, String>): Response<ArrayList<LoanModel>> =
        apiService.getLoanRecord(params)

    suspend fun getLoanInstallment(params: HashMap<String, String>): Response<ArrayList<InstallmentModel>> =
        apiService.getLoanInstallment(params)

    suspend fun getTotalPayment(params: HashMap<String, String>): Response<TotalModel> =
        apiService.getTotalPayment(params)


    suspend fun getMessage(params: HashMap<String, String>): Response<ArrayList<MessageModel>> =
        apiService.getMessage(params)

    suspend fun newMessage(params: HashMap<String, String>): Response<StringResponseModel> =
        apiService.newMessage(params)

    suspend fun getReply(params: HashMap<String, String>): Response<ReplyModel> =
        apiService.getReply(params)

    suspend fun getNews(): Response<ArrayList<NewsModel>> = apiService.getNews()

    suspend fun forgotPass(params: HashMap<String, String>): Response<ForgotPassModel> =
        apiService.forgotPassword(params)

    suspend fun getCompanyDetails(): Response<CompanyDetailsModel> = apiService.getCompanyDetails()

    suspend fun getAboutUS(): Response<AboutUsModel> = apiService.getAboutUs()

    suspend fun getRequests(params: HashMap<String, String>): Response<ArrayList<RequestModel>> =
        apiService.getRequests(params)

    suspend fun newRequest(params: HashMap<String, String>): Response<StringResponseModel> =
        apiService.newRequest(params)

    suspend fun requestReply(params: HashMap<String, String>): Response<RequestReplyModel> =
        apiService.requestReply(params)
}

