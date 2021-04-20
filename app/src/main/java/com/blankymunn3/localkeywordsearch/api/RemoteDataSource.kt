package com.blankymunn3.localkeywordsearch.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.model.State
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Header
import retrofit2.http.Query

class RemoteDataSource(
    private val kakaoAPI: KakaoAPI,
    private val compositeDisposable: CompositeDisposable,
    private val key: String,
    private val query: String,
    private val x: String,
    private val y: String
): PageKeyedDataSource<Int, Place>() {

    var state: MutableLiveData<State> = MutableLiveData()
    var isNextPage = false
    private var retryCompletable: Completable? = null

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Place>
    ) {
        if (!isNextPage) {
            updateState(State.LOADING)
            compositeDisposable.add(
                kakaoAPI.getSearchKeyword(key, query, x, y, params.key, params.requestedLoadSize)
                    .subscribe({ response ->
                        isNextPage = response.meta.isEnd
                        updateState(State.DONE)
                        callback.onResult(response.documents, params.key + 1)
                    }, {
                        it.printStackTrace()
                        updateState(State.ERROR)
                        setRetry { loadAfter(params, callback) }
                    })
            )
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Place>
    ) {
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Place>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            kakaoAPI.getSearchKeyword(key, query, x, y, 1, params.requestedLoadSize)
                .subscribe({
                    isNextPage = it.meta.isEnd
                    updateState(State.DONE)
                    callback.onResult(it.documents, null, 2)
                }, {
                    it.printStackTrace()
                    updateState(State.ERROR)
                    setRetry { loadInitial(params, callback) }
                })
        )
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    fun refresh() = this.invalidate()
}