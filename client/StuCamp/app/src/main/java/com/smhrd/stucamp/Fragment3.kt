package com.smhrd.stucamp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class Fragment3 : Fragment() {
//    lateinit var wv_kakaomap: WebView
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_3, container, false)

        Log.d("", "@@@@ KAKAO map start")

//        wv_kakaomap = view.findViewById(R.id.wv_kakaomap)

        // 여기서부터 추가 !
        val permissionCheck = ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
//        Log.d("어디서안댈까", "1")
//
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            Log.d("어디서안댈까", "2")
//
//            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            Log.d("어디서안댈까", "3")
//
//            try {
//                // 현위치 (위도,경도)
//                val userCurLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//                val uLatitude = userCurLocation!!.latitude
//                val uLongitude = userCurLocation.longitude
//
//
//                // 독서실, 스터디카페 정보 가져오기
//                // 독서실
//                val keyword = "독서실"
//                val searchRadius = 2000
//
////                val naverMapUri = "nmap://place?lat=$uLatitude&lng=$uLongitude&name=$keyword"
//                val naverMapUri = "nmap://search?query=$keyword&appname=com.smhrd.stucamp?lat=$uLatitude&lng=$uLongitude"
////                val naverMapUri = "nmap://search.naver.com/search.naver?query=$keyword&where=nexearch&sm=mtb_hty&ie=utf8"
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(naverMapUri))
//                startActivity(intent)
//
//
//            } catch (e: NullPointerException) {
//                Log.e("LOCATION_ERROR", e.toString())
//                Log.e("LOCATION_ERROR", e.toString())
//
//                ActivityCompat.finishAffinity(requireActivity())
//                System.exit(0)
//            }
//        } else {
//            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
//            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
//        }

        // 기존 데이터 없을경우 nullPointerException
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // ...
            val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {

                    val uLatitude = location.latitude
                    val uLongitude = location.longitude

                    val keyword = "스터디카페"

                    val naverMapUri = "nmap://search?query=$keyword&appname=com.smhrd.stucamp?lat=$uLatitude&lng=$uLongitude"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(naverMapUri))
                    startActivity(intent)

                    lm.removeUpdates(this)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            try {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, locationListener)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
        
        return view
    }
}