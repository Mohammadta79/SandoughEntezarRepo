package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.LoanModel
import com.example.sandoughentezar.models.PaymentModel
import com.example.sandoughentezar.repo.LoanRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanViewModel @Inject constructor(var repo: LoanRepo) : ViewModel() {

    private var loanRecordLD = MutableLiveData<Resource<ArrayList<LoanModel>>>()

    fun getLoanRecord(params: HashMap<String, String>): LiveData<Resource<ArrayList<LoanModel>>> {
        viewModelScope.launch {
            loanRecordLD.postValue(Resource.loading())
            repo.getLoanRecord(params)
                .flowOn(Dispatchers.IO)
                .catch { loanRecordLD.postValue(Resource.failure(it.toString())) }
                .collect { loanRecordLD.postValue(Resource.success(it)) }

        }
        return loanRecordLD
    }

}