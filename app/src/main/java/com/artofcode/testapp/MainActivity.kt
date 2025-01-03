package com.artofcode.testapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.artofcode.testapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDeviceName()
        setLocation()
    }

    private fun setDeviceName(){
        var manufacture= Build.MANUFACTURER
        var model= Build.MODEL

        binding.textDevice.text=manufacture+" "+model
    }
    private fun setLocation(){
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){

        }
        fusedLocation.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                val lat=latLng.latitude.toString()
                val long=latLng.longitude.toString()
                val address= getAddressFromLatLng(latLng.latitude,latLng.longitude)

                binding.textLattitude.text="Lattitude: "+lat
                binding.textLongitude.text="Longitude: "+long
                binding.textAddress.text="Current Address\n"+address

            }
        }
    }

    override fun onResume() {
        super.onResume()
        setDeviceName()
        setLocation()
    }
    private fun getAddressFromLatLng(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                address.getAddressLine(0) ?: "No address found"
            } else {
                "No address found"
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Error fetching address: ${e.message}")
            "Error fetching address"
        }
    }


}