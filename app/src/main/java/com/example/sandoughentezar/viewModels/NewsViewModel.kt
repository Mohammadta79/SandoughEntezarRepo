package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.NewsModel
import com.example.sandoughentezar.models.StringResponseModel
import com.example.sandoughentezar.repo.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(var repo: NewsRepo) : ViewModel() {

    private var newsLD = MutableLiveData<Resource<ArrayList<NewsModel>>>()

    fun getNews(): LiveData<Resource<ArrayList<NewsModel>>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                newsLD.postValue(Resource.loading())
                var response = repo.getNews()
                if (response.isSuccessful && response.body() != null) {
                    newsLD.postValue(Resource.success(response.body()) as Resource<ArrayList<NewsModel>>?)
                } else {
                    newsLD.postValue(Resource.failure(response.message()))
                }
            } catch (e: Exception) {
                newsLD.postValue(Resource.failure(e.toString()))
            }
        }
        return newsLD
    }
}