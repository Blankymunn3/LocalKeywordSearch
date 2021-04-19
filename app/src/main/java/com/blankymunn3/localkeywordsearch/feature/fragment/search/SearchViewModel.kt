package com.blankymunn3.localkeywordsearch.feature.fragment.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.repository.SearchRepository
import com.blankymunn3.localkeywordsearch.util.LiveData
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val searchRepository = SearchRepository()

    val kakaoKey = MutableLiveData("")
    val keyword = MutableLiveData("")

    val x = MutableLiveData("")
    val y = MutableLiveData("")
    val page = MutableLiveData(1)

    val documents: MutableLiveData<List<Place>> = MutableLiveData(emptyList())
    var list: ArrayList<Place> = ArrayList()


    fun getSearchKeywordResponse() {
        viewModelScope.launch {
            searchRepository.getSearchKeyword(
                key = kakaoKey.value!!,
                query = keyword.value!!,
                x = x.value!!,
                y = y.value!!,
                page = page.value!!,
                size = 10,
                onResponse = {
                    if (it.documents.isNotEmpty()) {
                        list.addAll(it.documents)
                        documents.postValue(list)
                    }
                },
                onFailure = {
                    it.printStackTrace()
                }
            )
        }
    }
}