package com.example.sandoughentezar.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.RequestModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.repo.RequestRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RequestViewModel @Inject constructor(var repo: RequestRepo) : ViewModel() {

    private var requestsLD = MutableLiveData<Resource<ArrayList<RequestModel>>>()
    private var newRequestsLD = MutableLiveData<Resource<StringResponseModel>>()

    fun getRequests(params: HashMap<String, String>): MutableLiveData<Resource<ArrayList<RequestModel>>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                requestsLD.postValue(Resource.loading())
                var response = repo.getRequests(params)
                if (response.isSuccessful && response.body() != null) {
                    requestsLD.postValue(Resource.success(response.body()) as Resource<ArrayList<RequestModel>>?)
                } else {
                    requestsLD.postValue(Resource.failure(response.message()))
                }

            } catch (e: Exception) {
                requestsLD.postValue(Resource.failure(e.toString()))
            }
        }
        return requestsLD
    }

    fun newRequest(params: HashMap<String, String>): MutableLiveData<Resource<StringResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                newRequestsLD.postValue(Resource.loading())
                var response = repo.newRequest(params)
                if (response.isSuccessful && response.body() != null) {
                    newRequestsLD.postValue(Resource.success(response.body()) as Resource<StringResponseModel>?)
                } else {
                    newRequestsLD.postValue(Resource.failure(response.message()))
                }

            } catch (e: Exception) {
                newRequestsLD.postValue(Resource.failure(e.toString()))
            }
        }
        return newRequestsLD
    }


}