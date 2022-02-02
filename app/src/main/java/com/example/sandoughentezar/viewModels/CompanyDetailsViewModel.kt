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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(var repo: CompanyDetailsRepo) : ViewModel() {

    private var companyDetailsLD: MutableLiveData<Resource<CompanyDetailsModel>> = MutableLiveData()
    private var aboutUsLD: MutableLiveData<Resource<AboutUsModel>> = MutableLiveData()

    fun getCompanyDetails(): MutableLiveData<Resource<CompanyDetailsModel>> {
        viewModelScope.launch {
            companyDetailsLD.postValue(Resource.loading())
            repo.getCompanyDetails()
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    companyDetailsLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    companyDetailsLD.postValue(Resource.success(it))

                }
        }
        return companyDetailsLD
    }

    fun getAboutUs(): MutableLiveData<Resource<AboutUsModel>> {
        viewModelScope.launch {
            aboutUsLD.postValue(Resource.loading())
            repo.getAboutUS()
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    aboutUsLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    aboutUsLD.postValue(Resource.success(it))

                }
        }
        return aboutUsLD
    }
}