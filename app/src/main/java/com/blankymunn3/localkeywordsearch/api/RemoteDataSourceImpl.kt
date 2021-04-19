package com.blankymunn3.localkeywordsearch.api

import com.blankymunn3.localkeywordsearch.api.APIClient.kakaoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RemoteDataSourceImpl: RemoteDataSource {

    lateinit var disposable: Disposable
    override fun getSearchKeyword(
        key: String,
        query: String,
        x: String,
        y: String,
        page: Int,
        size: Int,
        onResponse: (SearchKeywordResponse) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        disposable = kakaoAPI.getSearchKeyword(key, query, x, y, page, size)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                       onResponse(it)
            }, {
                onFailure(it)
            })
    }
}