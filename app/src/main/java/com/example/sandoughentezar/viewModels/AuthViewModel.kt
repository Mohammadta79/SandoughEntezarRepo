package com.example.sandoughentezar.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import com.example.sandoughentezar.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(var repo: AuthRepo) : ViewModel() {

    private val loginRes = MutableLiveData<Resource<LoginResponseModel>>()
    private val registerRes = MutableLiveData<Resource<StringResponseModel>>()
    private val validatePhoneRes = MutableLiveData<Resource<ValidatePhoneResponseModel>>()

    fun login(params: HashMap<String, String>): LiveData<Resource<LoginResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.login(params)
            loginRes.postValue(Resource.loading())

            try {
                if (response.isSuccessful && response.body() != null) {
                    loginRes.postValue(Resource.success(response.body()) as Resource<LoginResponseModel>?)
                } else {
                    loginRes.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                loginRes.postValue(Resource.failure(e.toString()))
            }
        }
        return loginRes
    }

    fun register(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.register(params)
            registerRes.postValue(Resource.loading())

            try {
                if (response.isSuccessful && response.body() != null) {
                    registerRes.postValue(Resource.success(response.body()) as Resource<StringResponseModel>)
                } else {
                    registerRes.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                registerRes.postValue(Resource.failure(e.toString()))
            }
        }
        return registerRes
    }

    fun validatePhone(params: HashMap<String, String>): LiveData<Resource<ValidatePhoneResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            validatePhoneRes.postValue(Resource.loading())
            val response = repo.validatePhone(params)
            try {
                if (response.isSuccessful && response.body() != null) {
                    validatePhoneRes.postValue(Resource.success(response.body()) as Resource<ValidatePhoneResponseModel>?)
                } else {
                    validatePhoneRes.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                validatePhoneRes.postValue(Resource.failure(e.toString()))
            }
        }
        return validatePhoneRes
    }


}