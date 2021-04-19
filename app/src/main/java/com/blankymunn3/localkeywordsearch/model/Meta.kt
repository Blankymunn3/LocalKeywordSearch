package com.blankymunn3.localkeywordsearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("is_end")
    @Expose
    var isEnd: Boolean
)