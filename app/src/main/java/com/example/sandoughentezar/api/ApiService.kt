package com.example.sandoughentezar.api

import com.example.sandoughentezar.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File


interface ApiService {

    @FormUrlEncoded
    @POST("login/")
    suspend fun login(@FieldMap params: HashMap<String, String>): Response<LoginResponseModel>

    @POST("register/")
    suspend fun register(
        @Body requestBody: RequestBody
    ): Response<StringResponseModel>

    @FormUrlEncoded
    @POST("deferredinstallments/")
    suspend fun getDeferredinstallments(@FieldMap params: HashMap<String, String>): Response<ArrayList<InstallmentModel>>


    @FormUrlEncoded
    @POST("userinfo/")
    suspend fun getUSerInfo(@FieldMap params: HashMap<String, String>): Response<UserModel>


    @FormUrlEncoded
    @POST("paymentrecord/")
    suspend fun getRecordPayment(@FieldMap params: HashMap<String, String>): Response<ArrayList<PaymentModel>>


    @FormUrlEncoded
    @POST("loanrecord/")
    suspend fun getLoanRecord(@FieldMap params: HashMap<String, String>): Response<ArrayList<LoanModel>>


    @FormUrlEncoded
    @POST("loaninstallment/")
    suspend fun getLoanInstallment(@FieldMap params: HashMap<String, String>): Response<ArrayList<InstallmentModel>>


    @FormUrlEncoded
    @POST("totalpayment/")
    suspend fun getTotalPayment(@FieldMap params: HashMap<String, String>): Response<TotalModel>


    @FormUrlEncoded
    @POST("getmessages/")
    suspend fun getMessage(@FieldMap params: HashMap<String, String>): Response<ArrayList<MessageModel>>

    @FormUrlEncoded
    @POST("getreply/")
    suspend fun getReply(@FieldMap params: HashMap<String, String>): Response<ReplyModel>


    @FormUrlEncoded
    @POST("newmessage/")
    suspend fun newMessage(@FieldMap params: HashMap<String, String>): Response<StringResponseModel>


    @GET("news/")
    suspend fun getNews(): Response<ArrayList<NewsModel>>


    @FormUrlEncoded
    @POST("sms/")
    suspend fun validatePhone(@FieldMap params: HashMap<String, String>): Response<ValidatePhoneResponseModel>

    @FormUrlEncoded
    @POST("score/")
    suspend fun getMyScore(@FieldMap params: HashMap<String, String>): Response<ScoreResponseModel>


    @FormUrlEncoded
    @POST("forgotpass/")
    suspend fun forgotPassword(@FieldMap params: HashMap<String, String>): Response<ForgotPassModel>


    @GET("getcompanydetails/")
    suspend fun getCompanyDetails(): Response<CompanyDetailsModel>

    @GET("getaboutus/")
    suspend fun getAboutUs(): Response<AboutUsModel>

    @FormUrlEncoded
    @POST("getrequests/")
    suspend fun getRequests(@FieldMap params: HashMap<String, String>): Response<ArrayList<RequestModel>>

    @FormUrlEncoded
    @POST("newrequest/")
    suspend fun newRequest(@FieldMap params: HashMap<String, String>):Response<StringResponseModel>
}