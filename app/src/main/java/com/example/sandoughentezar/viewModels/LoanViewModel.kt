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
        viewModelScope.launch {
            loanRecordLD.postValue(Resource.loading())
            repo.getLoanRecord(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    loanRecordLD.postValue(Resource.failure(_e.toString()))

                }.collect {
                    loanRecordLD.postValue(Resource.success(it))
                }
        }
        return loanRecordLD
    }

    fun getLoanInstallment(params: HashMap<String, String>): LiveData<Resource<ArrayList<InstallmentModel>>> {
        viewModelScope.launch {
            loanInstallmentLD.postValue(Resource.loading())
            repo.getLoanInstallment(params)
                .flowOn(Dispatchers.IO)
                .catch { _e ->
                    loanInstallmentLD.postValue(Resource.failure(_e.toString()))

                }.collect {
                    loanInstallmentLD.postValue(Resource.success(it))
                }
        }
        return loanInstallmentLD
    }

}