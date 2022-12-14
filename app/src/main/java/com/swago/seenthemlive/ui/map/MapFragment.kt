package com.swago.seenthemlive.ui.map

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.swago.seenthemlive.R
import com.swago.seenthemlive.ui.common.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var mapViewModel: MapViewModel

    private var loading: ContentLoadingProgressBar? = null
    private var mapFragment: SupportMapFragment? = null

    private var profileId: String = userId
    private var googleMap: GoogleMap? = null
    private var mapItems = mutableListOf<MapItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        loading = root.findViewById(R.id.loading)

        val fragmentManager = childFragmentManager
        val mapFragment = fragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        this.mapFragment = mapFragment
        mapFragment.getMapAsync(this)

        mapViewModel.userMapItems.observe(viewLifecycleOwner) {
            mapItems.clear()
            mapItems.addAll(it)
            updateMap()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun updateUi() {
        mapFragment?.view?.visibility = View.GONE
        loading?.show()
        mapViewModel.fetchUser(profileId) {
            mapFragment?.view?.visibility = View.VISIBLE
            loading?.hide()
        }
    }

    private fun updateMap() {
        val geocoder = Geocoder(requireContext())
        var minLat: Double? = null
        var maxLat: Double? = null
        var minLong: Double? = null
        var maxLong: Double? = null
        mapItems.forEach { mapItem ->
            if (mapItem.lat != null && mapItem.long != null) {
                if (mapItem.lat < (minLat ?: 90.0)) {
                    minLat = mapItem.lat
                }
                if (mapItem.lat > (maxLat ?: -90.0)) {
                    maxLat = mapItem.lat
                }
                if (mapItem.long < (minLong ?: 180.0)) {
                    minLong = mapItem.long
                }
                if (mapItem.long > (maxLong ?: -180.0)) {
                    maxLong = mapItem.long
                }
                mapItem.name.let { name ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocationName(
                            name!!,
                            1,
                            mapItem.lat - 2.0,
                            mapItem.long - 2.0,
                            mapItem.lat + 2.0,
                            mapItem.long + 2.0
                        ) { addresses ->
                            var lat = mapItem.lat
                            var long = mapItem.long
                            if (addresses.isNotEmpty()) {
                                addresses.first().let {
                                    lat = it.latitude
                                    long = it.longitude
                                }
                            } else {
                                Log.e("MapFragment", "Address not found for: $name")
                            }
                            GlobalScope.launch(Dispatchers.Main) {
                                googleMap?.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(lat!!, long!!))
                                        .title("${mapItem.name} (${mapItem.count} Concerts)")
                                )
                            }
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            val addresses = geocoder.getFromLocationName(
                                name!!,
                                1,
                                mapItem.lat - 2.0,
                                mapItem.long - 2.0,
                                mapItem.lat + 2.0,
                                mapItem.long + 2.0
                            )
                            var lat = mapItem.lat
                            var long = mapItem.long
                            if (addresses?.isNotEmpty() == true) {
                                addresses.first().let {
                                    lat = it.latitude
                                    long = it.longitude
                                }
                            } else {
                                Log.e("MapFragment", "Address not found for: $name")
                            }
                            launch(Dispatchers.Main) {
                                googleMap?.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(lat!!, long!!))
                                        .title("${mapItem.name} (${mapItem.count} Concerts)")
                                )
                            }
                        }
                    }
                }
                if (minLat != null && maxLat != null && minLong != null && maxLong != null) {
                    val bounds = LatLngBounds(
                        LatLng(minLat!! - 1.0, minLong!! - 1.0),  // SW bounds
                        LatLng(maxLat!! + 1.0, maxLong!! + 1.0) // NE bounds
                    )
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMap()
    }
}