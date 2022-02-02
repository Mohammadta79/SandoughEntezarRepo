package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.models.ScoreResponseModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.models.TotalModel
import com.example.sandoughentezar.repo.DashbordRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashbordViewModel @Inject constructor(var repo: DashbordRepo) : ViewModel() {
    private var myScoreLD = MutableLiveData<Resource<ScoreResponseModel>>()
    private var deffearedInstllment = MutableLiveData<Resource<ArrayList<InstallmentModel>>>()
    private var totalPaymentLD = MutableLiveData<Resource<TotalModel>>()

    fun getMyScore(params: HashMap<String, String>): LiveData<Resource<ScoreResponseModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                myScoreLD.postValue(Resource.loading())
                val response = repo.getMyScore(params)
                if (response.isSuccessful && response.body() != null) {
                    myScoreLD.postValue(Resource.success(response.body()) as Resource<ScoreResponseModel>?)
                } else {
                    myScoreLD.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                myScoreLD.postValue(Resource.failure(e.toString()))
            }
        }
        return myScoreLD
    }

    fun getDeffearedInstllment(params: HashMap<String, String>): LiveData<Resource<ArrayList<InstallmentModel>>> {
        viewModelScope.launch {
            deffearedInstllment.postValue(Resource.loading())
            repo.getDeferredinstallments(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    deffearedInstllment.postValue(Resource.failure(_e.toString()))
                }.collect {
                    deffearedInstllment.postValue(Resource.success(it))
                }
        }
        return deffearedInstllment
    }


    fun getTotalPayment(params: HashMap<String, String>): LiveData<Resource<TotalModel>> {
        viewModelScope.launch {
            totalPaymentLD.postValue(Resource.loading())
            repo.getTotalPayment(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    totalPaymentLD.postValue(Resource.failure(_e.toString()))
                }.collect {
                    totalPaymentLD.postValue(Resource.success(it))
                }
        }
        return totalPaymentLD
    }


}