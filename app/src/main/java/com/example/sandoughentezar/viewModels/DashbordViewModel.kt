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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                installmentPayLD.postValue(Resource.loading())
                var response = repo.installmentPay(params)
                if (response.isSuccessful && response.body() != null) {
                    installmentPayLD.postValue(Resource.success(response.body()) as Resource<StringResponseModel>?)
                } else {
                    installmentPayLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                installmentPayLD.postValue(Resource.failure(e.toString()))
            }
        }
        return installmentPayLD
    }

    fun getTotalPayment(params: HashMap<String, String>): LiveData<Resource<TotalModel>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                totalPaymentLD.postValue(Resource.loading())
                var response = repo.getTotalPayment(params)
                if (response.isSuccessful && response.body() != null) {
                    totalPaymentLD.postValue(Resource.success(response.body()) as Resource<TotalModel>?)
                } else {
                    totalPaymentLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                totalPaymentLD.postValue(Resource.failure(e.toString()))
            }
        }
        return totalPaymentLD
    }

    fun getLastLoan(params: HashMap<String, String>): LiveData<Resource<TotalModel>> {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                lastLoanLD.postValue(Resource.loading())
                var response = repo.getLastLoan(params)
                if (response.isSuccessful && response.body() != null) {
                    lastLoanLD.postValue(Resource.success(response.body()) as Resource<TotalModel>?)
                } else {
                    lastLoanLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                lastLoanLD.postValue(Resource.failure(e.toString()))
            }

        }
        return lastLoanLD
    }


}