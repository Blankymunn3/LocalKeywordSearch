package com.blankymunn3.localkeywordsearch.feature.activity.main

import android.util.Log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankymunn3.localkeywordsearch.api.APIClient.kakaoAPI
import com.blankymunn3.localkeywordsearch.repository.SearchRepository
import com.blankymunn3.localkeywordsearch.util.LiveData
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

}