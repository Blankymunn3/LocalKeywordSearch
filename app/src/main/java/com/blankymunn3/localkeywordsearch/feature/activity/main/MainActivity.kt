package com.blankymunn3.localkeywordsearch.feature.activity.main

import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.blankymunn3.localkeywordsearch.R
import com.blankymunn3.localkeywordsearch.databinding.ActivityMainBinding
import com.blankymunn3.localkeywordsearch.util.BaseActivity
import com.blankymunn3.localkeywordsearch.util.GetViewModel
import com.kakao.util.maps.helper.Utility

class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    val viewModel by GetViewModel(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this@MainActivity
        findNavController(R.id.nav_host_fragment)

        Log.e("Hash Key ::", Utility.getKeyHash(this))
    }
}