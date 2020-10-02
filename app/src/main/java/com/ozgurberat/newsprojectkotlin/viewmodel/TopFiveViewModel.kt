package com.ozgurberat.newsprojectkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozgurberat.newsprojectkotlin.requests.responses.NewsResponse
import com.ozgurberat.newsprojectkotlin.requests.ServiceGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopFiveViewModel : ViewModel() {

    private val _newsResponse = MutableLiveData<NewsResponse>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError =  MutableLiveData<Boolean>()

    val newsResponse: LiveData<NewsResponse>
        get() = _newsResponse

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val isError: LiveData<Boolean>
        get() = _isError


    private val api = ServiceGenerator.apiService

    fun getTopHeadlines(country: String, apiKey: String) {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = api.getTopHeadlines(country, apiKey)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _isError.value = false
                        _newsResponse.value = response.body()
                    }
                    else {
                        _isLoading.value = false
                        _isError.value = true
                    }
                }
            }
        }
    }

}




















