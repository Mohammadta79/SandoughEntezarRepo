package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.AboutUsModel
import com.example.sandoughentezar.models.CompanyDetailsModel
import com.example.sandoughentezar.repo.CompanyDetailsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(var repo: CompanyDetailsRepo) : ViewModel() {

    private var companyDetailsLD: MutableLiveData<Resource<CompanyDetailsModel>> = MutableLiveData()
    private var aboutUsLD: MutableLiveData<Resource<AboutUsModel>> = MutableLiveData()

    fun getCompanyDetails(): MutableLiveData<Resource<CompanyDetailsModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            companyDetailsLD.postValue(Resource.loading())
            try {
                var response = repo.getCompanyDetails()
                if (response.isSuccessful && response.body() != null) {
                    companyDetailsLD.postValue(Resource.success(response.body()) as Resource<CompanyDetailsModel>?)
                } else {
                    companyDetailsLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                companyDetailsLD.postValue(Resource.failure(e.toString()))
            }
        }
        return companyDetailsLD
    }

    fun getAboutUs(): MutableLiveData<Resource<AboutUsModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            aboutUsLD.postValue(Resource.loading())
            try {
                var response = repo.getAboutUS()
                if (response.isSuccessful && response.body() != null) {
                    aboutUsLD.postValue(Resource.success(response.body()) as Resource<AboutUsModel>?)
                } else {
                    aboutUsLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                aboutUsLD.postValue(Resource.failure(e.toString()))
            }
        }
        return aboutUsLD
    }
}