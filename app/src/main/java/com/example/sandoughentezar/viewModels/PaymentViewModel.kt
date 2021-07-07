package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.PaymentModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.repo.PaymentRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(var repo: PaymentRepo) : ViewModel() {
    private var paymentRecordLd = MutableLiveData<Resource<ArrayList<PaymentModel>>>()
    private var newPaymentLD = MutableLiveData<Resource<StringResponseModel>>()

    fun getPaymentRecords(params: HashMap<String, String>): LiveData<Resource<ArrayList<PaymentModel>>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                paymentRecordLd.postValue(Resource.loading())
                var response = repo.getRecordPayment(params)
                if (response.isSuccessful && response.body() != null) {
                    paymentRecordLd.postValue(Resource.success(response.body()) as Resource<ArrayList<PaymentModel>>?)
                }else{
                    paymentRecordLd.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                paymentRecordLd.postValue(Resource.failure(e.toString()))
            }
        }
        return paymentRecordLd
    }

    fun newPayment(params: HashMap<String, String>): LiveData<Resource<StringResponseModel>> {
        viewModelScope.launch {
            newPaymentLD.postValue(Resource.loading())
            repo.newPayment(params)
                .flowOn(Dispatchers.IO)
                .catch { newPaymentLD.postValue(Resource.failure(it.toString())) }
                .collect { newPaymentLD.postValue(Resource.success(it)) }

        }
        return newPaymentLD
    }

}