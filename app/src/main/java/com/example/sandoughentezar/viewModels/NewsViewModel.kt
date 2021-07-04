package com.example.sandoughentezar.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sandoughentezar.api.state.Resource
import com.example.sandoughentezar.models.NewsModel
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
        viewModelScope.launch {
            newsLD.postValue(Resource.loading())
            repo.getNews()
                .flowOn(Dispatchers.IO)
                .catch { newsLD.postValue(Resource.failure(it.toString())) }
                .collect { newsLD.postValue(Resource.success(it)) }
        }
        return newsLD
    }
}