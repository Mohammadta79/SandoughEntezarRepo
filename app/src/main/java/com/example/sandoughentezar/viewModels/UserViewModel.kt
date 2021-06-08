package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.LoginResponseModel
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

    private val userLD = MutableLiveData<Resource<UserModel>>()

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

}