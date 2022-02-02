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
    ): Flow<RegisterResponseModel> = flow { emit(apiService.register(requestBody)) }


    suspend fun getUserInfo(params: HashMap<String, String>): Flow<UserModel> =
        flow { emit(apiService.getUserInfo(params)) }

    suspend fun validatePhone(params: HashMap<String, String>): Flow<ValidatePhoneResponseModel> =
        flow { emit(apiService.validatePhone(params)) }

    suspend fun uploadImages(
        requestBody: RequestBody
    ): Flow<StringResponseModel> = flow { emit(apiService.uploadImages(requestBody)) }


    suspend fun getMyScore(params: HashMap<String, String>): Response<ScoreResponseModel> =
        apiService.getMyScore(params)

    suspend fun getDeferredinstallments(params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>> =
        flow { emit(apiService.getDeferredinstallments(params)) }


    suspend fun getRecordPayment(params: HashMap<String, String>): Flow<ArrayList<PaymentModel>> =
        flow { emit(apiService.getRecordPayment(params)) }


    suspend fun getLoanRecord(params: HashMap<String, String>): Flow<ArrayList<LoanModel>> =
        flow { emit(apiService.getLoanRecord(params)) }

    suspend fun getLoanInstallment(params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>> =
        flow { emit(apiService.getLoanInstallment(params)) }

    suspend fun getTotalPayment(params: HashMap<String, String>): Flow<TotalModel> =
        flow { emit(apiService.getTotalPayment(params)) }


    suspend fun getMessage(params: HashMap<String, String>): Flow<ArrayList<MessageModel>> =
        flow { emit(apiService.getMessage(params)) }

    suspend fun newMessage(params: HashMap<String, String>): Flow<StringResponseModel> =
        flow { emit(apiService.newMessage(params)) }

    suspend fun getReply(params: HashMap<String, String>): Flow<ReplyModel> =
        flow { emit(apiService.getReply(params)) }

    suspend fun getNews(): Flow<ArrayList<NewsModel>> = flow { emit(apiService.getNews()) }

    suspend fun forgotPass(params: HashMap<String, String>): Flow<ForgotPassModel> =
        flow { emit(apiService.forgotPassword(params)) }

    suspend fun getCompanyDetails(): Flow<CompanyDetailsModel> =
        flow { emit(apiService.getCompanyDetails()) }

    suspend fun getAboutUS(): Flow<AboutUsModel> = flow { emit(apiService.getAboutUs()) }

    suspend fun getRequests(params: HashMap<String, String>): Flow<ArrayList<RequestModel>> =
        flow { emit(apiService.getRequests(params)) }

    suspend fun newRequest(params: HashMap<String, String>): Flow<StringResponseModel> =
        flow { emit(apiService.newRequest(params)) }

    suspend fun requestReply(params: HashMap<String, String>): Flow<RequestReplyModel> =
        flow { emit(apiService.requestReply(params)) }
}

