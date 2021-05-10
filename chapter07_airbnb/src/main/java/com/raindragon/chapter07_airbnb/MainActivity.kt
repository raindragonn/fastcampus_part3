package com.raindragon.chapter07_airbnb

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.raindragon.chapter07_airbnb.databinding.ActivityMainBinding
import com.raindragon.chapter07_airbnb.model.House
import com.raindragon.chapter07_airbnb.model.HouseDto
import com.raindragon.chapter07_airbnb.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1010
    }

    private val TAG: String = MainActivity::class.java.simpleName
    private val viewPagerAdapter = HouseViewPagerAdapter(itemClickListener = {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "[지금 이 가격에 예약하세요!!] ${it.title} ${it.price} 사진보기 : ${it.imgUrl}"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent,null))
    })
    private val listAdapter = HouseListAdapter()
    private val markerClickListener = Overlay.OnClickListener { overlay ->

        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            binding.vpHouse.currentItem = position
        }

        return@OnClickListener true
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapView.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.apply {
            mapView.getMapAsync(this@MainActivity)


            bottomSheet.rv.layoutManager = LinearLayoutManager(this@MainActivity)
            bottomSheet.rv.adapter = listAdapter

            vpHouse.adapter = viewPagerAdapter
            vpHouse.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val selectedHouseModel = viewPagerAdapter.currentList[position]
                    val cameraUpdate =
                        CameraUpdate.scrollTo(
                            LatLng(
                                selectedHouseModel.lat,
                                selectedHouseModel.lng
                            )
                        ).animate(CameraAnimation.Easing)

                    naverMap.moveCamera(cameraUpdate)
                }
            })

        }
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.49788245591242, 127.02763088746462))
        naverMap.moveCamera(cameraUpdate)

        naverMap.uiSettings.apply {
            isLocationButtonEnabled = false
        }

        binding.btnCurrentLocation.map = naverMap

        // 현재 위치 가져오기
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        getHouseListFromAPI()
    }

    private fun getHouseListFromAPI() {
        NetworkManager.houseClient.getHouseList().enqueue(object : Callback<HouseDto> {
            override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                if (response.isSuccessful.not()) {
                    return
                }

                response.body()?.let { dto ->
                    updateMarker(dto.items)
                    viewPagerAdapter.submitList(dto.items)
                    listAdapter.submitList(dto.items)

                    binding.bottomSheet.tvTitle.text = "${dto.items.size}개의 숙소"
                }
            }

            override fun onFailure(call: Call<HouseDto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateMarker(houses: List<House>) {
        houses.forEach { house ->
            val marker = Marker()
            marker.position = LatLng(house.lat, house.lng)
            marker.map = naverMap
            marker.tag = house.id
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
            marker.onClickListener = markerClickListener
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}