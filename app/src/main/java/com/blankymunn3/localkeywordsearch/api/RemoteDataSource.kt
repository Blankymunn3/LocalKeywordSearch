package com.blankymunn3.localkeywordsearch.api

import retrofit2.http.Header
import retrofit2.http.Query

interface RemoteDataSource {
    fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        onResponse: (SearchKeywordResponse) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}