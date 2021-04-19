package com.blankymunn3.localkeywordsearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Place(
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("place_name")
    @Expose
    var placeName: String,

    @SerializedName("address_name")
    @Expose
    var addressName: String,

    @SerializedName("distance")
    @Expose
    var distance: String,

    @SerializedName("x")
    @Expose
    var x: String,

    @SerializedName("y")
    @Expose
    var y: String,

    @SerializedName("place_url")
    @Expose
    var placeUrl: String
)