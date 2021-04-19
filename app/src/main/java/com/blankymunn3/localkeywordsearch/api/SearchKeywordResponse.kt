package com.blankymunn3.localkeywordsearch.api

import com.blankymunn3.localkeywordsearch.model.Meta
import com.blankymunn3.localkeywordsearch.model.Place
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchKeywordResponse(
    @SerializedName("meta")
    @Expose
    var meta: Meta,

    @SerializedName("documents")
    @Expose
    var documents: List<Place>
)