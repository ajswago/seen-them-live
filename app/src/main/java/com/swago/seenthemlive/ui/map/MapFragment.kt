package com.swago.seenthemlive.ui.map

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
        var minLat: Double? = null
        var maxLat: Double? = null
        var minLong: Double? = null
        var maxLong: Double? = null
        mapItems.forEach {
            Log.d("MAP TEST", "Venue: ${it.name} lat: ${it.lat}, long: ${it.long}")
            if (it.lat != null && it.long != null) {
                if (it.lat < (minLat ?: 90.0)) {
                    Log.d("MAP TEST", "Set new min lat: ${it.lat}")
                    minLat = it.lat
                }
                if (it.lat > (maxLat ?: -90.0)) {
                    Log.d("MAP TEST", "Set new max lat: ${it.lat}")
                    maxLat = it.lat
                }
                if (it.long < (minLong ?: 180.0)) {
                    Log.d("MAP TEST", "Set new min long: ${it.long}")
                    minLong = it.long
                }
                if (it.long > (maxLong ?: -180.0)) {
                    Log.d("MAP TEST", "Set new min long: ${it.long}")
                    maxLong = it.long
                }
                googleMap?.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.lat, it.long)).title("${it.name} (${it.count} Concerts)")
                )
                Log.d("MAP TEST", "Bounds: min lat: ${minLat} max lat: ${maxLat} min long: ${minLong} max long: ${maxLong}")
                if (minLat != null && maxLat != null && minLong != null && maxLong != null) {
                    val bounds = LatLngBounds(
                        LatLng(minLat!!, minLong!!),  // SW bounds
                        LatLng(maxLat!!, maxLong!!) // NE bounds
                    )
                    Log.d("MAP TEST", "Update camera")
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