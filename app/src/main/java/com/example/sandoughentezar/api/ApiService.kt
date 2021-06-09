package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(@FieldMap params: HashMap<String, String>): Flow<LoginResponseModel>

    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>

    @FormUrlEncoded
    @POST("validatePhone.php")
    suspend fun validatePhone(@FieldMap params: HashMap<String, String>): Flow<ValidatePhoneResponseModel>

    @FormUrlEncoded
    @POST("getMyScore.php")
    suspend fun getMyScore(@FieldMap params: HashMap<String, String>): Flow<ScoreResponseModel>

    @FormUrlEncoded
    @POST("getDeferredinstallments.php")
    suspend fun getDeferredinstallments(@FieldMap params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>>

    @FormUrlEncoded
    @POST("installmentsPay.php")
    suspend fun installmentPay(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>


    @FormUrlEncoded
    @POST("getUserInfo.php")
    suspend fun getUSerInfo(@FieldMap params: HashMap<String, String>): Flow<UserModel>


    @FormUrlEncoded
    @POST("getRecordPayment.php")
    suspend fun getRecordPayment(@FieldMap params: HashMap<String, String>): Flow<ArrayList<PaymentModel>>

    @FormUrlEncoded
    @POST("newPayment.php")
    suspend fun newPayment(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>

    @FormUrlEncoded
    @POST("getLoanRecord.php")
    suspend fun getLoanRecord(@FieldMap params: HashMap<String, String>): Flow<ArrayList<LoanModel>>

    @FormUrlEncoded
    @POST("getLoanInstallment.php")
    suspend fun getLoanInstallment(@FieldMap params: HashMap<String, String>): Flow<ArrayList<InstallmentModel>>

    @FormUrlEncoded
    @POST("getTotalPayment.php")
    suspend fun getTotalPayment(@FieldMap params: HashMap<String, String>): Flow<TotalModel>

    @FormUrlEncoded
    @POST("getLastLoan.php")
    suspend fun getLastLoan(@FieldMap params: HashMap<String, String>): Flow<TotalModel>


    @FormUrlEncoded
    @POST("updateProfile.php")
    suspend fun updateProfile(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>


    @FormUrlEncoded
    @POST("getMessage.php")
    suspend fun getMessage(@FieldMap params: HashMap<String, String>): Flow<ArrayList<MessageModel>>

    @FormUrlEncoded
    @POST("newMessage.php")
    suspend fun newMessage(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>

    @FormUrlEncoded
    @POST("replyMessage.php")
    suspend fun replyMessage(@FieldMap params: HashMap<String, String>): Flow<StringResponseModel>
}