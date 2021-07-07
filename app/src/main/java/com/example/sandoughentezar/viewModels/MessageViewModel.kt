package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.MessageModel
import com.example.sandoughentezar.models.ReplyModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.repo.MessageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(var repo: MessageRepo) : ViewModel() {
    private var getMessageLD = MutableLiveData<Resource<ArrayList<MessageModel>>>()
    private var newMessageLD = MutableLiveData<Resource<StringResponseModel>>()
    private var replyMessageLD = MutableLiveData<Resource<ReplyModel>>()

    fun getMessage(params: HashMap<String, String>): LiveData<Resource<ArrayList<MessageModel>>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getMessageLD.postValue(Resource.loading())
                var response = repo.getMessage(params)
                if (response.isSuccessful && response.body() != null) {
                    getMessageLD.postValue(Resource.success(response.body()) as Resource<ArrayList<MessageModel>>?)
                } else {
                    getMessageLD.postValue(Resource.failure(response.message()))
                }

            } catch (e: Exception) {
                getMessageLD.postValue(Resource.failure(e.toString()))
            }

        }
        return getMessageLD
    }

    fun newMessage(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                newMessageLD.postValue(Resource.loading())
                var response = repo.newMessage(params)
                if (response.isSuccessful && response.body() != null) {
                    newMessageLD.postValue(Resource.success(response.body()) as Resource<StringResponseModel>?)
                } else {
                    newMessageLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                newMessageLD.postValue(Resource.failure(e.toString()))
            }
        }
        return newMessageLD
    }

    fun getReply(params: HashMap<String, String>): LiveData<Resource<ReplyModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                replyMessageLD.postValue(Resource.loading())
                var response = repo.getReply(params)
                if (response.isSuccessful && response.body() != null) {
                    replyMessageLD.postValue(Resource.success(response.body()) as Resource<ReplyModel>?)
                } else {
                    replyMessageLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                replyMessageLD.postValue(Resource.failure(e.toString()))
            }
        }
        return replyMessageLD
    }
}