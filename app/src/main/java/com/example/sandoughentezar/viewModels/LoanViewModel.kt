package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.InstallmentModel
import com.example.sandoughentezar.models.LoanModel
import com.example.sandoughentezar.models.PaymentModel
import com.example.sandoughentezar.repo.LoanRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(var repo: LoanRepo) : ViewModel() {

    private var loanRecordLD = MutableLiveData<Resource<ArrayList<LoanModel>>>()
    private var loanInstallmentLD = MutableLiveData<Resource<ArrayList<InstallmentModel>>>()

    fun getLoanRecord(params: HashMap<String, String>): LiveData<Resource<ArrayList<LoanModel>>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loanRecordLD.postValue(Resource.loading())
                var response = repo.getLoanRecord(params)
                if (response.isSuccessful && response.body() != null) {
                    loanRecordLD.postValue(Resource.success(response.body()) as Resource<ArrayList<LoanModel>>?)
                }else{
                    loanRecordLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                loanRecordLD.postValue(Resource.failure(e.toString()))

            }
        }
        return loanRecordLD
    }

    fun getLoanInstallment(params: HashMap<String, String>): LiveData<Resource<ArrayList<InstallmentModel>>> {
        viewModelScope.launch {
            try {
                loanInstallmentLD.postValue(Resource.loading())
                var response = repo.getLoanInstallment(params)
                if (response.isSuccessful && response.body() != null) {
                    loanInstallmentLD.postValue(Resource.success(response.body()) as Resource<ArrayList<InstallmentModel>>?)
                }else{
                    loanInstallmentLD.postValue(Resource.failure(response.message()))
                }
            }catch (e:Exception){
                loanInstallmentLD.postValue(Resource.failure(e.toString()))
            }
        }
        return loanInstallmentLD
    }

}