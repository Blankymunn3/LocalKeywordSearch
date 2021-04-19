package com.blankymunn3.localkeywordsearch.feature.fragment.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.R
import com.blankymunn3.localkeywordsearch.adapter.SearchRVAdapter
import com.blankymunn3.localkeywordsearch.databinding.FragmentSearchBinding
import com.blankymunn3.localkeywordsearch.feature.activity.main.MainActivity
import com.blankymunn3.localkeywordsearch.feature.activity.webview.WebViewActivity
import com.blankymunn3.localkeywordsearch.util.GetViewModel
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
        searchRVAdapter = SearchRVAdapter()
        binding.rvSearchResponse.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            setHasFixedSize(true)
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
                }
                return true
            }
        })

        searchRVAdapter.setOnClickItem(object : SearchRVAdapter.OnClickItemListener {
            override fun onClickItem(position: Int) {
                val intent = Intent(activity, WebViewActivity::class.java)
                intent.putExtra("EXTRA_TITLE", viewModel.documents.value!![position].placeName)
                intent.putExtra("EXTRA_URL", viewModel.documents.value!![position].placeUrl)
                startActivity(intent)
            }
        })

        binding.rvSearchResponse.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (binding.rvSearchResponse.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = binding.rvSearchResponse.adapter!!.itemCount
                if (lastVisibleItemPosition == itemTotalCount) viewModel.page.postValue(viewModel.page.value!! + 1)
            }
        })

        viewModel.keyword.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) viewModel.getSearchKeywordResponse()
        })

        viewModel.documents.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                searchRVAdapter.setData(it)
            }
        })

        viewModel.page.observe(viewLifecycleOwner, {
            if (it > 1) viewModel.getSearchKeywordResponse()
        })
    }
}