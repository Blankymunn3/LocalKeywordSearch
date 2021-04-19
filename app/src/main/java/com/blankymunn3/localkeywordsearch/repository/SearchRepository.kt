package com.blankymunn3.localkeywordsearch.repository

import com.blankymunn3.localkeywordsearch.api.RemoteDataSourceImpl
import com.blankymunn3.localkeywordsearch.api.SearchKeywordResponse

class SearchRepository {
    private val remoteDataSource = RemoteDataSourceImpl()

    fun getSearchKeyword(
        key: String,
        query: String,
        x: String,
        y: String,
        page: Int,
        size: Int,
        onResponse: (SearchKeywordResponse) -> Unit,
        onFailure: (Throwable) -> Unit) {
        remoteDataSource.getSearchKeyword(key, query, x, y, page, size, onResponse, onFailure)
    }
}