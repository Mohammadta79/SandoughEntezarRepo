package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.models.ScoreResponseModel
import com.example.sandoughentezar.models.StringResponseModel
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
    private var installmentPayLD = MutableLiveData<Resource<StringResponseModel>>()

    fun getMyScore(params: HashMap<String, String>): LiveData<Resource<ScoreResponseModel>> {
        viewModelScope.launch {
            myScoreLD.postValue(Resource.loading())
            repo.getMyScore(params)
                .flowOn(Dispatchers.IO)
                .catch { myScoreLD.postValue(Resource.failure(it.toString())) }
                .collect { myScoreLD.postValue(Resource.success(it)) }
        }
        return myScoreLD
    }

    fun getDeffearedInstllment(params: HashMap<String, String>): LiveData<Resource<ArrayList<InstallmentModel>>> {
        viewModelScope.launch {
            deffearedInstllment.postValue(Resource.loading())
            repo.getDeferredinstallments(params)
                .flowOn(Dispatchers.IO)
                .catch { deffearedInstllment.postValue(Resource.failure(it.toString())) }
                .collect { deffearedInstllment.postValue(Resource.success(it)) }
        }
        return deffearedInstllment
    }

    fun installmentPay(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch {
            installmentPayLD.postValue(Resource.loading())
            repo.installmentPay(params)
                .flowOn(Dispatchers.IO)
                .catch { installmentPayLD.postValue(Resource.failure(it.toString())) }
                .collect { installmentPayLD.postValue(Resource.success(it)) }
        }
        return installmentPayLD
    }


}