package com.blankymunn3.localkeywordsearch.feature.fragment.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blankymunn3.localkeywordsearch.util.LiveData

class MapViewModel: ViewModel() {

    val x = MutableLiveData("")
    val y = MutableLiveData("")

    val isLocation = LiveData().mediatorLiveData(x, y) {
        x.value!!.isNotEmpty() && y.value!!.isNotEmpty()
    }
}