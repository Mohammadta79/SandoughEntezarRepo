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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userLD.postValue(Resource.loading())
                var response = repo.getUserInfo(params)
                if (response.isSuccessful && response.body() != null) {
                    userLD.postValue(Resource.success(response.body()) as Resource<UserModel>?)
                }else{
                    userLD.postValue(Resource.failure(response.message()))
                }

            } catch (e: Exception) {
                userLD.postValue(Resource.failure(e.toString()))
            }


        }
        return userLD
    }


    fun updateUser(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserLD.postValue(Resource.loading())
            try {
                var response = repo.updateProfile(params)
                if (response.isSuccessful && response.body()!=null){
                    updateUserLD.postValue(Resource.success(response.body()) as Resource<StringResponseModel>?)
                }else{
                    updateUserLD.postValue(Resource.failure(response.message()))
                }

            }catch (e:Exception){
                updateUserLD.postValue(Resource.failure(e.toString()))
            }

        }
        return updateUserLD
    }
}