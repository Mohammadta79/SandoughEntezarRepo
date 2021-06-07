package com.example.sandoughentezar.viewModels

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
        viewModelScope.launch {
            loginRes.postValue(Resource.loading())
            repo.login(params)
                .flowOn(Dispatchers.IO)
                .catch {
                    loginRes.postValue(Resource.failure(this.toString()))
                }
                .collect {
                    loginRes.postValue(Resource.success(it))
                }

        }
        return loginRes
    }

    fun register(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch {
            registerRes.postValue(Resource.loading())
            repo.register(params)
                .flowOn(Dispatchers.IO)
                .catch {
                    registerRes.postValue(Resource.failure(this.toString()))
                }
                .collect {
                    registerRes.postValue(Resource.success(it))
                }

        }
        return registerRes
    }

    fun validatePhone(params: HashMap<String, String>): LiveData<Resource<ValidatePhoneResponseModel>> {
        viewModelScope.launch {
            registerRes.postValue(Resource.loading())
            repo.validatePhone(params)
                .flowOn(Dispatchers.IO)
                .catch {
                    validatePhoneRes.postValue(Resource.failure(this.toString()))
                }
                .collect {
                    validatePhoneRes.postValue(Resource.success(it))
                }

        }
        return validatePhoneRes
    }


}