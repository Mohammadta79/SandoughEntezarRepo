package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.LoginResponseModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.UserModel
import com.example.sandoughentezar.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(var repo: UserRepo) : ViewModel() {

    private var userLD = MutableLiveData<Resource<UserModel>>()
    private var updateUserLD = MutableLiveData<Resource<StringResponseModel>>()

    fun getUSerInfo(params: HashMap<String, String>): LiveData<Resource<UserModel>> {
        viewModelScope.launch {
            userLD.postValue(Resource.loading())
            repo.getUserInfo(params)
                .flowOn(Dispatchers.IO)
                .catch { userLD.postValue(Resource.failure(it.toString())) }
                .collect { userLD.postValue(Resource.success(it)) }

        }
        return userLD
    }


    fun updateUser(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch {
            updateUserLD.postValue(Resource.loading())
            repo.updateProfile(params)
                .flowOn(Dispatchers.IO)
                .catch { updateUserLD.postValue(Resource.failure(it.toString())) }
                .collect { updateUserLD.postValue(Resource.success(it)) }

        }
        return updateUserLD
    }
}