package com.example.sandoughentezar.repo

import com.example.sandoughentezar.api.ApiServiceResult
import com.example.sandoughentezar.models.AboutUsModel
import com.example.sandoughentezar.models.CompanyDetailsModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class CompanyDetailsRepo @Inject constructor(val apiServiceResult: ApiServiceResult) {

    suspend fun getCompanyDetails(): Flow<CompanyDetailsModel> =
        apiServiceResult.getCompanyDetails()

    suspend fun getAboutUS(): Flow<AboutUsModel> = apiServiceResult.getAboutUS()
}