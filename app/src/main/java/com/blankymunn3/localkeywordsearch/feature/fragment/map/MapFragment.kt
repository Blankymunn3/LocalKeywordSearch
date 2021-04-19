package com.blankymunn3.localkeywordsearch.feature.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.blankymunn3.localkeywordsearch.databinding.FragmentMapBinding
import com.blankymunn3.localkeywordsearch.databinding.FragmentSearchBinding
import com.blankymunn3.localkeywordsearch.feature.activity.main.MainActivity
import com.blankymunn3.localkeywordsearch.util.GetViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.transition.MaterialFadeThrough
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment: Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel by GetViewModel(MapViewModel::class.java)

    private lateinit var activity: MainActivity

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
    @SuppressLint("MissingPermission")
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (entry in it.entries) {
                if (entry.value) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            viewModel.x.postValue(location.latitude.toString())
                            viewModel.y.postValue(location.longitude.toString())
                        }
                    }
                }
            }
        }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        permissionLauncher.launch(permissions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@MapFragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnClickListener {
            val direction: NavDirections = MapFragmentDirections.navToSearchFragment(1)
            findNavController().navigate(direction)
        }
        val mapView = MapView(getActivity())
        val mapViewContainer: ViewGroup = binding.mapView
        mapView.zoomIn(true)
        mapView.zoomOut(true)
        mapViewContainer.addView(mapView)

        viewModel.isLocation.observe(viewLifecycleOwner, {
            if (it) mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(viewModel.x.value!!.toDouble(), viewModel.y.value!!.toDouble()), 1, true)
        })
    }
}