package com.blankymunn3.localkeywordsearch.api

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.blankymunn3.localkeywordsearch.model.Place
import io.reactivex.disposables.CompositeDisposable

class RemoteDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private var kakaoAPI: KakaoAPI,
    private var key: String,
    private var query: String,
    private var x: String,
    private var y: String,
): DataSource.Factory<Int, Place>() {

    val remoteDataSourceLiveData = MutableLiveData<RemoteDataSource>()
    override fun create(): DataSource<Int, Place> {
        val remoteDataSource = RemoteDataSource(kakaoAPI, compositeDisposable, key, query, x, y)
        remoteDataSourceLiveData.postValue(remoteDataSource)
        return remoteDataSource
    }

    fun getQuery() = query

    fun searchItem(key: String,
                   query: String,
                   x: String,
                   y: String,) {
        this.key = key
        this.query = query
        this.x = x
        this.y = y
        remoteDataSourceLiveData.value?.refresh()
    }
}