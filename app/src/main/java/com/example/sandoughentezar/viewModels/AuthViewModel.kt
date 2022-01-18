package com.example.sandoughentezar.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.ForgotPassModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.ValidatePhoneResponseModel
import com.example.sandoughentezar.repo.AuthRepo
import com.example.sandoughentezar.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(var repo: AuthRepo) : ViewModel() {

    private val loginRes = SingleLiveEvent<Resource<LoginResponseModel>>()
    private val registerRes = SingleLiveEvent<Resource<StringResponseModel>>()
    private val uploadImagesLD = SingleLiveEvent<Resource<StringResponseModel>>()
    private val validatePhoneRes = MutableLiveData<Resource<ValidatePhoneResponseModel>>()
    private val forgotPass = MutableLiveData<Resource<ForgotPassModel>>()

    fun login(map: HashMap<String, String>): LiveData<Resource<LoginResponseModel>> {
        viewModelScope.launch {
            loginRes.postValue(Resource.loading())
            repo.login(map)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    loginRes.postValue(Resource.failure(_e.toString()))
                }.collect {
                    loginRes.postValue(Resource.success(it))
                }
        }
        return loginRes
    }

    fun register(
        requestBody: RequestBody
    ): SingleLiveEvent<Resource<StringResponseModel>> {
        viewModelScope.launch {
            registerRes.postValue(Resource.loading())
            repo.register(
                requestBody
            ).flowOn(Dispatchers.IO)
                .catch { _e ->
                    registerRes.postValue(Resource.failure(_e.toString()))
                }
                .collect {
                    registerRes.postValue(Resource.success(it))
                }
        }
        return registerRes
    }

    fun validatePhone(params: HashMap<String, String>): LiveData<Resource<ValidatePhoneResponseModel>> {
        viewModelScope.launch {
            validatePhoneRes.postValue(Resource.loading())
            repo.validatePhone(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    validatePhoneRes.postValue(Resource.failure(_e.toString()))
                }.collect {
                    validatePhoneRes.postValue(Resource.success(it))
                }
        }
        return validatePhoneRes
    }

    fun forgotPass(params: HashMap<String, String>): LiveData<Resource<ForgotPassModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            forgotPass.postValue(Resource.loading())
            val response = repo.forgotPass(params)
            try {
                if (response.isSuccessful && response.body() != null) {
                    forgotPass.postValue(Resource.success(response.body()) as Resource<ForgotPassModel>?)
                } else {
                    forgotPass.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                forgotPass.postValue(Resource.failure(e.toString()))
            }
        }
        return forgotPass
    }

    fun uploadImages(

        requestBody: RequestBody
    ): SingleLiveEvent<Resource<StringResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            uploadImagesLD.postValue(Resource.loading())
            val response = repo.uploadImages(requestBody)
            try {
                if (response.isSuccessful && response.body() != null) {
                    uploadImagesLD.postValue(Resource.success(response.body()) as Resource<StringResponseModel>?)
                } else {
                    uploadImagesLD.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                uploadImagesLD.postValue(Resource.failure(e.toString()))
            }
        }
        return uploadImagesLD
    }


}