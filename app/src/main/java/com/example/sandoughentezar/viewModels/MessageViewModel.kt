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
        viewModelScope.launch {

            getMessageLD.postValue(Resource.loading())
            repo.getMessage(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    getMessageLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    getMessageLD.postValue(Resource.success(it))

                }
        }
        return getMessageLD
    }

    fun newMessage(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {

        viewModelScope.launch {

            newMessageLD.postValue(Resource.loading())
            repo.newMessage(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    newMessageLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    newMessageLD.postValue(Resource.success(it))
                }
        }
        return newMessageLD
    }

    fun getReply(params: HashMap<String, String>): LiveData<Resource<ReplyModel>> {
        viewModelScope.launch {
            replyMessageLD.postValue(Resource.loading())
            repo.getReply(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    replyMessageLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    replyMessageLD.postValue(Resource.success(it))
                }
        }
        return replyMessageLD
    }
}