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
    private var installmentPayLD = MutableLiveData<Resource<StringResponseModel>>()
    private var lastLoanLD = MutableLiveData<Resource<TotalModel>>()
    private var totalPaymentLD = MutableLiveData<Resource<TotalModel>>()

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deffearedInstllment.postValue(Resource.loading())
                val response = repo.getDeferredinstallments(params)
                if (response.isSuccessful && response.body() != null) {
                    deffearedInstllment.postValue(Resource.success(response.body()) as Resource<ArrayList<InstallmentModel>>?)
                } else {
                    deffearedInstllment.postValue(Resource.failure(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                deffearedInstllment.postValue(Resource.failure(e.toString()))
            }
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

    fun getTotalPayment(params: HashMap<String, String>): LiveData<Resource<TotalModel>> {
        viewModelScope.launch {
            totalPaymentLD.postValue(Resource.loading())
            repo.getTotalPayment(params)
                .flowOn(Dispatchers.IO)
                .catch { totalPaymentLD.postValue(Resource.failure(it.toString())) }
                .collect { totalPaymentLD.postValue(Resource.success(it)) }
        }
        return totalPaymentLD
    }

    fun getLastLoan(params: HashMap<String, String>): LiveData<Resource<TotalModel>> {
        viewModelScope.launch {
            lastLoanLD.postValue(Resource.loading())
            repo.getLastLoan(params)
                .flowOn(Dispatchers.IO)
                .catch { lastLoanLD.postValue(Resource.failure(it.toString())) }
                .collect { lastLoanLD.postValue(Resource.success(it)) }
        }
        return lastLoanLD
    }


}