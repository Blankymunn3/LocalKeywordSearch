package com.blankymunn3.localkeywordsearch.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoAPI {
    @GET("v2/local/search/keyword.json")
    fun getSearchKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("radius") radius: Int = 4000
    ): Observable<SearchKeywordResponse>
}