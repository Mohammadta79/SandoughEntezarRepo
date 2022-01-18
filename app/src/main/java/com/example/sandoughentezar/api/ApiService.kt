package com.example.sandoughentezar.api

import androidx.media.AudioAttributesCompat
import com.example.sandoughentezar.models.*
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File


interface ApiService {

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(
        @FieldMap params: HashMap<String, String>
    ): LoginResponseModel

    @POST("/api/register_personal_data")
    suspend fun register(
        @Body requestBody: RequestBody
    ): StringResponseModel


    @POST("/api/register/images")
    suspend fun uploadImages(
        @Body requestBody: RequestBody
    ): Response<StringResponseModel>

    @FormUrlEncoded
    @POST("/api/deferredinstallments")
    suspend fun getDeferredinstallments(@FieldMap params: HashMap<String, String>): Response<ArrayList<InstallmentModel>>


    @FormUrlEncoded
    @POST("/api/memberinfo")
    suspend fun getUserInfo(@FieldMap params: HashMap<String, String>): UserModel


    @FormUrlEncoded
    @POST("/api/paymentrecord")
    suspend fun getRecordPayment(@FieldMap params: HashMap<String, String>): Response<ArrayList<PaymentModel>>


    @FormUrlEncoded
    @POST("/api/loanrecord")
    suspend fun getLoanRecord(@FieldMap params: HashMap<String, String>): Response<ArrayList<LoanModel>>


    @FormUrlEncoded
    @POST("/api/loaninstallment")
    suspend fun getLoanInstallment(@FieldMap params: HashMap<String, String>): Response<ArrayList<InstallmentModel>>


    @FormUrlEncoded
    @POST("/api/totalpayment")
    suspend fun getTotalPayment(@FieldMap params: HashMap<String, String>): Response<TotalModel>


    @FormUrlEncoded
    @POST("/api/getmessages")
    suspend fun getMessage(@FieldMap params: HashMap<String, String>): Response<ArrayList<MessageModel>>

    @FormUrlEncoded
    @POST("/api/getreply")
    suspend fun getReply(@FieldMap params: HashMap<String, String>): Response<ReplyModel>


    @FormUrlEncoded
    @POST("/api/newmessage")
    suspend fun newMessage(@FieldMap params: HashMap<String, String>): Response<StringResponseModel>


    @GET("/api/news")
    suspend fun getNews(): Response<ArrayList<NewsModel>>


    @FormUrlEncoded
    @POST("/api/sms")
    suspend fun validatePhone(@FieldMap params: HashMap<String, String>): ValidatePhoneResponseModel


    @FormUrlEncoded
    @POST("/api/score")
    suspend fun getMyScore(@FieldMap params: HashMap<String, String>): Response<ScoreResponseModel>


    @FormUrlEncoded
    @POST("/api/forgotpass")
    suspend fun forgotPassword(@FieldMap params: HashMap<String, String>): Response<ForgotPassModel>


    @GET("/api/getcompanydetails")
    suspend fun getCompanyDetails(): Response<CompanyDetailsModel>

    @GET("/api/getaboutus")
    suspend fun getAboutUs(): Response<AboutUsModel>

    @FormUrlEncoded
    @POST("/api/getrequests")
    suspend fun getRequests(@FieldMap params: HashMap<String, String>): Response<ArrayList<RequestModel>>

    @FormUrlEncoded
    @POST("/api/newrequest")
    suspend fun newRequest(@FieldMap params: HashMap<String, String>): Response<StringResponseModel>

    @FormUrlEncoded
    @POST("/api/requestreply")
    suspend fun requestReply(@FieldMap params: HashMap<String, String>): Response<RequestReplyModel>
}