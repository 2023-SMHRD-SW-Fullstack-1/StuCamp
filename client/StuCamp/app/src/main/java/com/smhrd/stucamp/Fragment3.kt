package com.smhrd.stucamp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapFragment
import com.smhrd.stucamp.databinding.Fragment3Binding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.lang.NullPointerException


class Fragment3 : Fragment() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_3, container, false)

        // Initialize the MapView
        val mapView = MapView(requireActivity())

        // Add the MapView to the container
        val mapViewContainer = view.findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(35.14982, 126.919953), 1, true);

        



        // Return the view
        return view


    }
}