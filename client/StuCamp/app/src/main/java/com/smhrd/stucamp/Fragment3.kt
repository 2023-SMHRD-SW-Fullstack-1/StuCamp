package com.smhrd.stucamp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapFragment
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class Fragment3 : Fragment() {

    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        Log.d("","@@@@ KAKAO map start")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_3, container, false)

        // Initialize the MapView
        val mapView = MapView(requireActivity())

        // Add the MapView to the container
        val mapViewContainer = view.findViewById<ViewGroup>(R.id.map_view)
        mapViewContainer.addView(mapView)

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(35.14982, 126.919953), 1, true);


        // 여기서부터 추가 !
        val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {

                // 현위치 (위도,경도)
                val userCurLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val uLatitude = userCurLocation!!.latitude
                val uLogitude = userCurLocation.longitude
                val uCurPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLogitude)
                mapView.setMapCenterPoint(uCurPosition, true)

                // 현 위치에 마커 찍기
                val marker = MapPOIItem()
                marker.itemName = "현 위치" // 마커 클릭 시 텍스트 출력
                marker.mapPoint = uCurPosition
                marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
                marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                mapView.addPOIItem(marker)



            }catch(e: NullPointerException){
                Log.e("LOCATION_ERROR", e.toString())
                Log.e("LOCATION_ERROR", e.toString())

                ActivityCompat.finishAffinity(requireActivity())

                val intent = Intent(context, MapFragment::class.java)
                startActivity(intent)
                System.exit(0)
            }
        }else{
            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }






        // Return the view
        return view
    }
}