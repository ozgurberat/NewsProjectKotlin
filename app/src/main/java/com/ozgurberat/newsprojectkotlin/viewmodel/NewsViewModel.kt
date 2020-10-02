package com.ozgurberat.newsprojectkotlin.viewmodel

import androidx.lifecycle.*
import com.ozgurberat.newsprojectkotlin.requests.responses.NewsResponse
import com.ozgurberat.newsprojectkotlin.requests.ServiceGenerator
import kotlinx.coroutines.*

class NewsViewModel : ViewModel() {

    private val TAG = "NewsViewModel"

    val _newsResponseList = MutableLiveData<ArrayList<NewsResponse>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Boolean>()

    val newsResponseList: LiveData<ArrayList<NewsResponse>>
        get() = _newsResponseList

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val isError: LiveData<Boolean>
        get() = _isError

    private val api = ServiceGenerator.apiService


    suspend fun getEverything(topic: String, apiKey: String): NewsResponse? {
        withContext(Dispatchers.Main) {
            _isLoading.value = true
        }
        val response = withContext(Dispatchers.IO) {
            api.getEverything(topic, apiKey)
        }

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                _isLoading.value = false
                _isError.value = false
                println("Response is successful: ${response.code()}")
            }
            else {
                _isLoading.value = false
                _isError.value = true
            }
        }
        return response.body()
    }

    fun updateLiveData(responseList: ArrayList<NewsResponse>) {
        CoroutineScope(Dispatchers.Main).launch {
            _newsResponseList.value = responseList
            println("response list : ${responseList.size}")
            responseList.clear()
        }

    }

}










