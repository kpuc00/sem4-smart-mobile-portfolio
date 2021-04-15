package com.kpuc.showyourlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kpuc.showyourlocation.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var locationManager: LocationManager? = null
    private var receivedLocation: LatLng? = null
    private var firstLoaded: Boolean = false

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.clear()
        googleMap.addMarker(receivedLocation?.let {
            MarkerOptions().position(it)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .alpha(0.5F)
        })
        if (!firstLoaded) {
            followLocation()
            firstLoaded = true
        }
    }

    private val goToMyLocation = OnMapReadyCallback { googleMap ->
        if (receivedLocation != null) {
            val cameraPosition = CameraPosition.Builder()
                .target(receivedLocation)
                .zoom(15F).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        } else {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.getting_location),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnMyLocation = findViewById<Button>(R.id.btnMyLocation)
        btnMyLocation.setOnClickListener {
            followLocation()
        }
        checkLocationPermissions()
    }

    private fun followLocation() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(goToMyLocation)
    }

    private fun updateMapLocation() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun startLocationUpdates() {
        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.location_exception),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
            val speed = location.speed
            val speedKmH = (speed * 3.6).roundToInt()
            val longitude = BigDecimal(location.longitude).setScale(6, RoundingMode.HALF_EVEN)
            val latitude = BigDecimal(location.latitude).setScale(6, RoundingMode.HALF_EVEN)
            receivedLocation = LatLng(location.latitude, location.longitude)
            binding.tvSpeed.text = speedKmH.toString() + resources.getString(R.string.speed)
            binding.tvLocationLongitude.text = "x: $longitude"
            binding.tvLocationLatitude.text = "y: $latitude"
            updateMapLocation()
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startLocationUpdates()
            } else {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            -> {

            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }
}