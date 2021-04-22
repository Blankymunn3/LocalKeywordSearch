package com.blankymunn3.localkeywordsearch.feature.fragment.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.R
import com.blankymunn3.localkeywordsearch.adapter.SearchRVAdapter
import com.blankymunn3.localkeywordsearch.databinding.FragmentSearchBinding
import com.blankymunn3.localkeywordsearch.feature.activity.main.MainActivity
import com.blankymunn3.localkeywordsearch.feature.activity.webview.WebViewActivity
import com.blankymunn3.localkeywordsearch.model.State
import com.blankymunn3.localkeywordsearch.util.GetViewModel
import com.blankymunn3.localkeywordsearch.util.OnClickItemListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment: Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by GetViewModel(SearchViewModel::class.java)

    private lateinit var activity: MainActivity

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchRVAdapter: SearchRVAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        viewModel.kakaoKey.postValue("KakaoAK ${activity.getString(R.string.kakao_rest_api_key)}")
        fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                Log.e("Location", "${location.longitude}, ${location.latitude}")
                viewModel.x.postValue(location.longitude.toString())
                viewModel.y.postValue(location.latitude.toString())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@SearchFragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRVAdapter = SearchRVAdapter { viewModel.retry() }
        binding.rvSearchResponse.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            isNestedScrollingEnabled = false
            adapter = searchRVAdapter
        }
        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                CoroutineScope(Main).launch {
                    delay(500)
                    viewModel.keyword.postValue(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Main).launch {
                    delay(500)
                    viewModel.keyword.postValue(newText)
                }
                return true
            }
        })

        searchRVAdapter.setOnClickItem(object : OnClickItemListener {
            override fun onClickItem(position: Int) {
                val intent = Intent(activity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_TITLE", viewModel.searchList.value!![position]!!.placeName)
                intent.putExtra("EXTRA_URL", viewModel.searchList.value!![position]!!.placeUrl)
                startActivity(intent)
                activity.overridePendingTransition(R.anim.slide_left, R.anim.hold)
            }
        })

        viewModel.keyword.observe(viewLifecycleOwner, {
            viewModel.getSearchResponse()
        })

        viewModel.searchList.observe(viewLifecycleOwner, {
            searchRVAdapter.submitList(it)
        })

        viewModel.getState().observe(viewLifecycleOwner, {
            if (!viewModel.listIsEmpty()) searchRVAdapter.setState(it ?: State.DONE)
        })
    }
}