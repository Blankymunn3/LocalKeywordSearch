package com.blankymunn3.localkeywordsearch.feature.fragment.search

import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.blankymunn3.localkeywordsearch.api.APIClient
import com.blankymunn3.localkeywordsearch.api.KakaoAPI
import com.blankymunn3.localkeywordsearch.api.RemoteDataSource
import com.blankymunn3.localkeywordsearch.api.RemoteDataSourceFactory
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.model.State
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val kakaoAPI = APIClient.kakaoAPI
    var searchList: LiveData<PagedList<Place>>
    private val compositeDisposable = CompositeDisposable()
    private var remoteDataSourceFactory: RemoteDataSourceFactory
    private val pageSize = 10

    val kakaoKey = MutableLiveData("")
    val keyword = MutableLiveData("")

    val x = MutableLiveData("")
    val y = MutableLiveData("")

    init {
        remoteDataSourceFactory = RemoteDataSourceFactory(compositeDisposable, kakaoAPI,
            kakaoKey.value!!, keyword.value!!, x.value!!, y.value!!)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        searchList = LivePagedListBuilder(remoteDataSourceFactory, config).build()
    }

    fun getSearchResponse() {
        if (remoteDataSourceFactory.getQuery() == keyword.value!!) return
        remoteDataSourceFactory.searchItem(kakaoKey.value!!, keyword.value!!, x.value!!, y.value!!)
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        remoteDataSourceFactory.remoteDataSourceLiveData,
        RemoteDataSource::state
    )

    fun retry() {
        remoteDataSourceFactory.remoteDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return searchList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}