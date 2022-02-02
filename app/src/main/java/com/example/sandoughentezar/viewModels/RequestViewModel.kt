package com.example.sandoughentezar.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.RequestModel
import com.example.sandoughentezar.models.RequestReplyModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.repo.RequestRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RequestViewModel @Inject constructor(var repo: RequestRepo) : ViewModel() {

    private var requestsLD = MutableLiveData<Resource<ArrayList<RequestModel>>>()
    private var newRequestsLD = MutableLiveData<Resource<StringResponseModel>>()
    private var replyRequest = MutableLiveData<Resource<RequestReplyModel>>()

    fun getRequests(params: HashMap<String, String>): MutableLiveData<Resource<ArrayList<RequestModel>>> {
        viewModelScope.launch {
            requestsLD.postValue(Resource.loading())
            repo.getRequests(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    requestsLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    requestsLD.postValue(Resource.success(it))
                }
        }
        return requestsLD
    }

    fun newRequest(params: HashMap<String, String>): MutableLiveData<Resource<StringResponseModel>> {
        viewModelScope.launch {

            newRequestsLD.postValue(Resource.loading())
            repo.newRequest(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    newRequestsLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    newRequestsLD.postValue(Resource.success(it))

                }
        }
        return newRequestsLD
    }

    fun requestReply(params: HashMap<String, String>): MutableLiveData<Resource<RequestReplyModel>> {
        viewModelScope.launch {

            replyRequest.postValue(Resource.loading())
            repo.requestReply(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    replyRequest.postValue(Resource.failure(_e.toString()))
                }.collect {
                    replyRequest.postValue(Resource.success(it))
                }
        }
        return replyRequest
    }


}